package com.example.vaadinDemo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.util.StringUtils;

public class MainView extends VerticalLayout {
    private final CustomerRepository customerRepository;
    private final CustomerEditor customerEditor;
    final Grid<Customer> grid;
    final TextField filter;
    private final Button addNewButton;

    public MainView(CustomerRepository customerRepository, CustomerEditor customerEditor) {
        this.customerRepository = customerRepository;
        this.customerEditor = customerEditor;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNewButton = new Button("New Customer", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        add(actions, grid, actions);

        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastname");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        filter.setPlaceholder("Filter by last name");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener( e -> {
            this.customerEditor.editCustomer(e.getValue());
        });

        addNewButton.addClickListener(e -> customerEditor.editCustomer(new Customer()));

        customerEditor.setChangeHandler(() -> {
            customerEditor.setVisible(false);

        });

    }

    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(customerRepository.findAll());
        } else {
            grid.setItems(customerRepository.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }
}
