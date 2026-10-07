/*
 *     Copyright (c) 2026, Blacksheep Tech.
 *     All rights reserved.
 *
 */

package org.eclipse.store.demo.bookstore.util;

import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.server.Command;

import java.util.function.Consumer;

public class RendererFactory {

    /**
     * Returns a renderer for the ViewBooks with view inventory and new book button
     *
     * @param viewConsumer The view consumer callback
     * @param createAction The create action callback
     * @param <T>          The entity type
     * @return The Renderer
     */
    public static <T> LitRenderer<T> getBooksActionRenderer(Consumer<T> viewConsumer, Command createAction) {

        return LitRenderer.<T>of(
                        "<div>" +
                                "<vaadin-button theme='tertiary-inline icon' " +
                                "@click='${viewInventory}' title='${item.viewTitle}'>" +
                                "<vaadin-icon icon='vaadin:stock'></vaadin-icon>" +
                                "</vaadin-button>" +

                                " " +

                                "<vaadin-button theme='tertiary-inline icon' " +
                                "@click='${newBook}' title='${item.newTitle}'>" +
                                "<vaadin-icon icon='vaadin:plus-circle'></vaadin-icon>" +
                                "</vaadin-button>" +
                                "</div>"
                )
                //Tooltip
                .withProperty("viewTitle", item -> "Show Inventory")
                .withProperty("newTitle", item -> "New Book")

                //On click
                .withFunction("viewInventory", viewConsumer::accept)
                .withFunction("newBook", item -> createAction.execute());
    }

    /**
     * Returns a renderer for the ViewShop with the view inventory and purchase buttons
     *
     * @param inventoryConsumer The view inventory callback
     * @param purchaseConsumer  The view purchase callback
     * @param <T>               The entity type
     * @return The Renderer
     */
    public static <T> LitRenderer<T> getShopActionRenderer(Consumer<T> inventoryConsumer, Consumer<T> purchaseConsumer) {

        return LitRenderer.<T>of(
                        "<div>" +
                                "<vaadin-button theme='tertiary-inline icon' " +
                                "@click='${viewInventory}' title='${item.inventoryTitle}'>" +
                                "<vaadin-icon icon='vaadin:stock'></vaadin-icon>" +
                                "</vaadin-button>" +

                                " " +

                                "<vaadin-button theme='tertiary-inline icon' " +
                                "@click='${newBook}' title='${item.purchaseTitle}'>" +
                                "<vaadin-icon icon='vaadin:cart'></vaadin-icon>" +
                                "</vaadin-button>" +
                                "</div>"
                )
                //Tooltip
                .withProperty("inventoryTitle", item -> "Show Inventory")
                .withProperty("purchaseTitle", item -> "Show Purchases")

                //On click
                .withFunction("viewInventory", inventoryConsumer::accept)
                .withFunction("newBook", purchaseConsumer::accept);
    }

}
