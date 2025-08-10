/*
 * Copyright (c) 2023 JZomDev
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tabsviewer;

import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EquipmentViewerOverlay extends OverlayPanel
{
	private static final int PANEL_GAP_X = 5;
	private static final int PANEL_GAP_Y = 4;

	private static final int ITEM_BIAS_X = 3;
	private static final int ITEM_BIAS_Y = 0;

	private final ItemManager itemManager;
	private final Client client;
	private final EquipmentViewerConfig config;

	private final BufferedImage slotBg;

	private final int slotW;
	private final int slotH;
	private final int phWidth;  // placeholder width (slot width)
	private final int phHeight; // placeholder height (slot height)

	// Alignment derived from panel gap + slot width
	private final int row2MarginX; // replaces magic 15 in row 2
	private final int phWidth150;  // wide spacers in rows 1 & 4

	@Inject
	private EquipmentViewerOverlay(Client client, ItemManager itemManager, EquipmentViewerConfig config)
	{
		setPriority(Overlay.PRIORITY_HIGH);
		setPosition(OverlayPosition.BOTTOM_RIGHT);

		panelComponent.setWrap(true);
		panelComponent.setGap(new Point(PANEL_GAP_X, PANEL_GAP_Y));
		panelComponent.setPreferredSize(new Dimension(3 * (Constants.ITEM_SPRITE_WIDTH + 16), 0));
		panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);

		this.itemManager = itemManager;
		this.client = client;
		this.config = config;

		this.slotBg = ImageUtil.loadImageResource(getClass(), "/com/tabsviewer/slot.png");

		// Derive sizes from the actual image; fall back to 36x32 if it failed to load
		int w = (slotBg != null ? slotBg.getWidth() : Constants.ITEM_SPRITE_WIDTH);
		int h = (slotBg != null ? slotBg.getHeight() : Constants.ITEM_SPRITE_HEIGHT);

		this.slotW = w;
		this.slotH = h;
		this.phWidth = w;
		this.phHeight = h;

		// Alignment math (fixes row 2 drift)
		this.row2MarginX = 7 + PANEL_GAP_X; // e.g., 7 + 5 = 12
		this.phWidth150  = slotW + PANEL_GAP_X + row2MarginX;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (config.hideWhenEquipmentTabOpen() && isEquipmentTabOpen())
		{
			return null;
		}

		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		setPriority(isEquipmentTabOpen() ? Overlay.PRIORITY_LOW : Overlay.PRIORITY_HIGH);

		panelComponent.setBackgroundColor(config.panelBgColor());

		final ItemContainer itemContainer = client.getItemContainer(InventoryID.EQUIPMENT);
		if (itemContainer == null)
		{
			return null;
		}

		final Item[] items = itemContainer.getItems();

		ArrayList<ArrayList<Item>> loop = getEquipment(items);
		panelComponent.getChildren().clear();

		buildFirstRow(loop.get(0));
		buildSecondRow(loop.get(1));
		buildThirdRow(loop.get(2));
		buildForthRow(loop.get(3));
		buildFifthRow(loop.get(4));

		Dimension dim = panelComponent.render(graphics);

		if (dim != null && config.panelBorderThickness() > 0)
		{
			Stroke old = graphics.getStroke();
			Color oldColor = graphics.getColor();
			int t = config.panelBorderThickness();
			int r = config.panelCornerRadius();

			int inset = t / 2;
			graphics.setStroke(new BasicStroke(t));
			graphics.setColor(config.panelBorderColor());
			graphics.drawRoundRect(inset, inset, dim.width - t, dim.height - t, r, r);

			graphics.setStroke(old);
			graphics.setColor(oldColor);
		}

		return dim;
	}

	private boolean isEquipmentTabOpen()
	{
		// Root of the Equipment side panel (group 387).
		Widget root = client.getWidget(WidgetID.EQUIPMENT_GROUP_ID, 0);
		return root != null && !root.isHidden();
	}

	private void buildFirstRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0 || j == 2)
			{
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(phWidth150, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
			}
			else
			{
				addSlot(row.get(j));
			}
		}
	}

	private void buildSecondRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(row2MarginX, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
				addSlot(row.get(j));
			}
			else if (j == 1)
			{
				addSlot(row.get(j));
			}
			else if (j == 2)
			{
				addSlot(row.get(j));
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(row2MarginX, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
			}
		}
	}

	private void buildThirdRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(3,
					phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
				addSlot(row.get(j));
			}
			else if (j == 1)
			{
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(4,
					phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
				addSlot(row.get(j));
			}
			else if (j == 2)
			{
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(2,
					phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
				addSlot(row.get(j));
			}
		}
	}

	private void buildForthRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(9, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(phWidth150, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
			}
			else if (j == 2)
			{
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(phWidth150, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
			}
			else
			{
				addSlot(row.get(j));
			}
		}
	}

	private void buildFifthRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(3, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
				addSlot(row.get(j));
			}
			else if (j == 1)
			{
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(2, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
				addSlot(row.get(j));
			}
			else if (j == 2)
			{
				panelComponent.getChildren().add(new ImageComponent(
					new BufferedImage(2, phHeight, BufferedImage.TYPE_4BYTE_ABGR)));
				addSlot(row.get(j));
			}
		}
	}

	private void addSlot(Item item)
	{
		final boolean showBg = config.showSlotBackgrounds();
		final BufferedImage itemImg = getItemSprite(item);

		// Always compose on the fly so opacity changes apply instantly
		panelComponent.getChildren().add(new ImageComponent(composeSlot(itemImg, showBg)));
	}

	private BufferedImage composeSlot(BufferedImage itemImg, boolean drawBackground)
	{
		BufferedImage out = new BufferedImage(slotW, slotH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		try
		{
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

			if (drawBackground && slotBg != null)
			{
				float bgAlpha = Math.max(0f, Math.min(1f, config.slotBgOpacity() / 100f));
				Composite old = g.getComposite();
				if (bgAlpha < 1f)
				{
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bgAlpha));
				}
				g.drawImage(slotBg, 0, 0, null);
				if (bgAlpha < 1f)
				{
					g.setComposite(old);
				}

				Color tint = config.slotBgColor();
				if (tint.getAlpha() > 0)
				{
					Composite old2 = g.getComposite();
					float tintAlpha = (tint.getAlpha() / 255f);
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, tintAlpha));
					g.setColor(new Color(tint.getRed(), tint.getGreen(), tint.getBlue()));
					g.fillRect(0, 0, slotW, slotH);
					g.setComposite(old2);
				}
			}

			if (itemImg != null)
			{
				int iw = itemImg.getWidth();
				int ih = itemImg.getHeight();
				int x = Math.round((slotW - iw) / 2f) + ITEM_BIAS_X;
				int y = Math.round((slotH - ih) / 2f) + ITEM_BIAS_Y;
				g.drawImage(itemImg, x, y, null);
			}
		}
		finally
		{
			g.dispose();
		}
		return out;
	}

	private BufferedImage getItemSprite(Item item)
	{
		if (item == null || item.getQuantity() <= 0)
		{
			return null;
		}
		return itemManager.getImage(item.getId(), item.getQuantity(), item.getQuantity() > 1);
	}

	private ArrayList<ArrayList<Item>> getEquipment(Item[] items)
	{
		ArrayList<Item> row1 = new ArrayList<>();
		addItemIfExists(row1, items, -1);
		addItemIfExists(row1, items, 0);
		addItemIfExists(row1, items, -1);

		ArrayList<Item> row2 = new ArrayList<>();
		addItemIfExists(row2, items, 1);
		addItemIfExists(row2, items, 2);
		addItemIfExists(row2, items, 13);

		ArrayList<Item> row3 = new ArrayList<>();
		addItemIfExists(row3, items, 3);
		addItemIfExists(row3, items, 4);
		addItemIfExists(row3, items, 5);

		ArrayList<Item> row4 = new ArrayList<>();
		addItemIfExists(row4, items, 6);
		addItemIfExists(row4, items, 7);
		addItemIfExists(row4, items, 8);

		ArrayList<Item> row5 = new ArrayList<>();
		addItemIfExists(row5, items, 9);
		addItemIfExists(row5, items, 10);
		addItemIfExists(row5, items, 12);

		ArrayList<ArrayList<Item>> out = new ArrayList<>();
		out.add(row1);
		out.add(row2);
		out.add(row3);
		out.add(row4);
		out.add(row5);

		return out;
	}

	private void addItemIfExists(ArrayList<Item> row, Item[] items, int index)
	{
		if (index == -1)
		{
			row.add(null);
		}
		else if (index >= items.length)
		{
			row.add(null);
		}
		else
		{
			row.add(items[index]);
		}
	}
}
