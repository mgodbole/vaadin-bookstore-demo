package org.eclipse.store.demo.bookstore.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.eclipse.store.demo.bookstore.BookStoreDemo;
import org.eclipse.store.demo.bookstore.data.Book;
import org.eclipse.store.demo.bookstore.data.Books;
import org.eclipse.store.demo.bookstore.util.RendererFactory;

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
 * View to display and modify {@link Books}.
 *
 */
@Route(value = "books", layout = RootLayout.class)
public class ViewBooks extends ViewEntity<Book> {

    public ViewBooks() {
        super();
    }

    @Override
    protected void createUI() {
        this.addGridColumnWithTextFilter("title", Book::title);
        this.addGridColumnWithDynamicFilter("author", Book::author);
        this.addGridColumnWithDynamicFilter("genre", Book::genre);
        this.addGridColumnWithDynamicFilter("publisher", Book::publisher);
        this.addGridColumnWithDynamicFilter("language", Book::language);
        this.addGridColumnWithTextFilter("isbn13", Book::isbn13);

        //Action column
        this.grid.addColumn(RendererFactory.getBooksActionRenderer(this::showInventory, this::doCreateNewBook))
                .setAutoWidth(true).setFlexGrow(0)
                .setHeader("⚒️").setTextAlign(ColumnTextAlign.CENTER);

        final Button showInventoryButton = new Button(
                this.getTranslation("showInventory"),
                VaadinIcon.STOCK.create(),
                event -> this.showInventory(this.getSelectedEntity())
        );
        showInventoryButton.setEnabled(false);
        this.grid.addSelectionListener(event -> {
            final boolean b = event.getFirstSelectedItem().isPresent();
            showInventoryButton.setEnabled(b);
        });

        final Button createBookButton = new Button(
                this.getTranslation("createBook"),
                VaadinIcon.PLUS_CIRCLE.create(),
                event -> this.doCreateNewBook()
        );

        var header = new HorizontalLayout(showInventoryButton, createBookButton);
        header.addClassNames(LumoUtility.Padding.End.MEDIUM);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.END);

        this.add(header);
    }

    private void showInventory(final Book book) {
        this.getUI()
                .flatMap(ui -> ui.navigate(ViewInventory.class))
                .ifPresent(view -> view.filterBy(book));
    }

    private void doCreateNewBook() {
        openCreateBookDialog();
    }

    private void openCreateBookDialog() {
        DialogBookCreate.open(book -> {
            BookStoreDemo.getInstance().data().books().add(book);
            this.listEntities();
        });
    }


    @Override
    public <R> R compute(final SerializableFunction<Stream<Book>, R> function) {
        return BookStoreDemo.getInstance().data().books().compute(function);
    }
}
