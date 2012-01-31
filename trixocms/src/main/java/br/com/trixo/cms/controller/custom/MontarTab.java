/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.custom;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;

/**
 * Teste.
 * @author Orlei Bicheski e Rafael Abreu
 */
@Named
public class MontarTab extends CustomController {

    /**
     * 
     */
    private static final long serialVersionUID = -2411794408738010330L;

    private TabView tabView;

    @PostConstruct
    public void load() {

        System.out.println("load()");

        tabView = new TabView();

        Tab tab1 = new Tab();
        Tab tab2 = new Tab();

        tab1.setTitle("Abreu");
        tab2.setTitle("Orlei");

        tabView.getChildren().add(tab1);
        tabView.getChildren().add(tab2);
    }

    public void krlho() {

        System.out.println("KRLHO");
    }
}
