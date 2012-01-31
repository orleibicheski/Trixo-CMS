/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.controller.custom.tab;

import javax.faces.component.UIComponent;

import org.primefaces.event.TabChangeEvent;

import br.com.trixo.cms.controller.custom.CustomController;

/**
 * TransactionController que armazena na sessão dados do painel do site. Estes
 * dados servem para manter os componentes desejados em um estado, equanto o
 * usuário navega pelo site.
 * 
 * @author Rafael Abreu
 * @version $Revision$
 */
@SuppressWarnings("serial")
public abstract class GravarEstadoPainel extends CustomController {

    /**
     * Index da tab selecioada.
     */
    private Integer tabIndexSelecionado;

    /**
     * @return Retorna index da tab selecionada.
     */
    public Integer getTabIndexSelecionado() {
	return tabIndexSelecionado;
    }

    /**
     * @param tabIndexSelecionado
     *            Index tab selelcionado para ser setado.
     */
    public void setTabIndexSelecionado(Integer tabIndexSelecionado) {
	this.tabIndexSelecionado = tabIndexSelecionado;
    }

    /**
     * Listener para controlar o estado da Tab.
     * 
     * @param event
     *            Evento relacionado a alterações no estado da tab.
     */
    public void tabChangeListener(TabChangeEvent event) {

	// TODO testar isso um dia para ver se arrumaram.
	// TabView tab = (TabView) event.getTab().getParent();
	// this.tabIndexSelecionado = tab.getActiveIndex();

	// TODO remover esse código abaixo quando o código acima funcionar
	String tabSelecionado = event.getTab().getId();

	int tabIndexSelecionado = 0;

	for (UIComponent comp : event.getTab().getParent().getChildren()) {
	    if (comp.getId().equals(tabSelecionado)) {
		break;
	    }
	    tabIndexSelecionado++;
	}

	this.setTabIndexSelecionado(tabIndexSelecionado);
    }

}
