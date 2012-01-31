/**
 * Copyright (c) 2003-2011 Trixo, Infoeski Consultoria e Informatica Ltda.
 * Todos os direitos reservados.
 */
package br.com.trixo.cms.entity.publicacao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 */
@Entity
@DiscriminatorValue("AGENDADA")
public class PublicacaoAgendada extends Publicacao {

    /**
     * serial ID.
     */
    private static final long serialVersionUID = -8022819445671492520L;

    /**
     * Data de execução do agendamento.
     */
    @Column(nullable = true)
    @NotNull
    private Date agendamento;

    /**
     * @return the agendamento
     */
    public Date getAgendamento() {
        return agendamento;
    }

    /**
     * @param agendamento
     *            the agendamento to set
     */
    public void setAgendamento(Date agendamento) {
        this.agendamento = agendamento;
    }
}
