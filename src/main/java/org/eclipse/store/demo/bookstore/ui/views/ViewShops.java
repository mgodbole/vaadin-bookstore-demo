package org.eclipse.store.demo.bookstore.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;
import org.eclipse.store.demo.bookstore.BookStoreDemo;
import org.eclipse.store.demo.bookstore.data.Shop;
import org.eclipse.store.demo.bookstore.data.Shops;
import org.eclipse.store.demo.bookstore.util.RendererFactory;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.stream.Stream;

/*-
 * #%L
 * EclipseStore BookStore Demo
 * %%
 * Copyright (C) 2023 MicroStream Software
 * %%
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * #L%
 */

/**
 * View to display {@link Shops}.
 *
 */
@Route(value = "shops", layout = RootLayout.class)
public class ViewShops extends ViewNamedWithAddress<Shop> {
    public ViewShops() {
        super();
    }

    @Override
    protected void createUI() {
        this.addGridColumnForName();
        this.addGridColumnsForAddress();

        //Action column
        this.grid.addColumn(RendererFactory.getShopActionRenderer(this::showInventory, this::showPurchases))
                .setAutoWidth(true).setFlexGrow(0)
                .setHeader("⚒️").setTextAlign(ColumnTextAlign.CENTER);

        final Button showInventoryButton = new Button(
                this.getTranslation("showInventory"),
                VaadinIcon.STOCK.create(),
                event -> this.showInventory(this.getSelectedEntity())
        );
        final Button showPurchasesButton = new Button(
                this.getTranslation("showPurchases"),
                LineAwesomeIcon.SHOPPING_CART_SOLID.create(),
                event -> this.showPurchases(this.getSelectedEntity())
        );

        showInventoryButton.setEnabled(false);
        showPurchasesButton.setEnabled(false);
        this.grid.addSelectionListener(event -> {
            final boolean b = event.getFirstSelectedItem().isPresent();
            showInventoryButton.setEnabled(b);
            showPurchasesButton.setEnabled(b);
        });

        this.add(new HorizontalLayout(showInventoryButton, showPurchasesButton));
    }

    private void showInventory(final Shop shop) {
        this.getUI()
                .flatMap(ui -> ui.navigate(ViewInventory.class))
                .ifPresent(view -> view.filterBy(shop));
    }

    private void showPurchases(final Shop shop) {
        this.getUI()
                .flatMap(ui -> ui.navigate(ViewPurchases.class))
                .ifPresent(view -> view.filterBy(shop));
    }

    @Override
    public <R> R compute(final SerializableFunction<Stream<Shop>, R> function) {
        return BookStoreDemo.getInstance().data().shops().compute(function);
    }

}
