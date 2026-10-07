package org.eclipse.store.demo.bookstore.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.eclipse.store.demo.bookstore.BookStoreDemo;
import org.eclipse.store.demo.bookstore.data.Customer;
import org.eclipse.store.demo.bookstore.data.Customers;
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
 * View to display {@link Customers}.
 *
 */
@Route(value = "customers", layout = RootLayout.class)
public class ViewCustomers extends ViewNamedWithAddress<Customer> {

    public ViewCustomers() {
        super();
    }

    @Override
    protected void createUI() {
        this.addGridColumn("id", Customer::customerId);
        this.addGridColumnForName();
        this.addGridColumnsForAddress();

        //Action column
        this.grid.addColumn(RendererFactory.getCustomerActionRenderer(this::showPurchases))
                .setAutoWidth(true).setFlexGrow(0)
                .setHeader("⚒️").setTextAlign(ColumnTextAlign.CENTER);

        final Button showPurchasesButton = new Button(
                this.getTranslation("showPurchases"),
                LineAwesomeIcon.SHOPPING_CART_SOLID.create(),
                event -> this.showPurchases(this.getSelectedEntity())
        );

        showPurchasesButton.setEnabled(false);
        this.grid.addSelectionListener(event -> {
            final boolean b = event.getFirstSelectedItem().isPresent();
            showPurchasesButton.setEnabled(b);
        });

        var header = new HorizontalLayout(showPurchasesButton);
        header.addClassNames(LumoUtility.Padding.End.MEDIUM);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.END);

        this.add(header);
    }

    @Override
    public <R> R compute(final SerializableFunction<Stream<Customer>, R> function) {
        return BookStoreDemo.getInstance().data().customers().compute(function);
    }

    private void showPurchases(final Customer customer) {
        this.getUI()
                .flatMap(ui -> ui.navigate(ViewPurchases.class))
                .ifPresent(view -> view.filterBy(customer));
    }

}
