package com.example.vaadinDemo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements KeyNotifier {
    private final CustomerRepository repository;

    // current customer
    private Customer customer;

    // fields to edit properties in Customer entity
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last Name");

    //action buttons

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Customer> binder = new Binder<>(Customer.class);
    private ChangeHandler changeHandler;

    public CustomerEditor(CustomerRepository repository) {
        this.repository = repository;

        add(firstName, lastName, actions);
        binder.bindInstanceFields(this);

        // configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());

        //wire caction buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
    }

    void delete() {
        repository.delete(customer);
        changeHandler.onChange();
    }

    void save() {
        repository.save(customer);
        changeHandler.onChange();
    }

    public interface ChangeHandler{
        void onChange();
    }

    public final void editCustomer(Customer customer) {
        if (customer == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = customer.getId() != null;

        if (persisted) {
            this.customer = repository.findById(customer.getId()).get();
        } else {
            this.customer = customer;
        }
        cancel.setVisible(persisted);
        binder.setBean(customer);

        setVisible(true);
        firstName.focus();
    }

    public void setChangeHandler(ChangeHandler handler) {
        changeHandler = handler;
    }
}
