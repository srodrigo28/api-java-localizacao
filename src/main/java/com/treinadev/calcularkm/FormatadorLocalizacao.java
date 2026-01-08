package com.treinadev.calcularkm;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Responsável por centralizar formatações dependentes de Locale (datas, números, distâncias).
 * Comentários em português para facilitar manutenção.
 */
public final class FormatadorLocalizacao {

    private FormatadorLocalizacao() {}

    /**
     * Formata valores numéricos conforme o Locale.
     * Use para exibir quilômetros, litros, etc., sem concatenar símbolos manualmente.
     */
    public static String formatarNumero(double valor, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(2); // Regra: até 2 casas decimais para leitura
        return nf.format(valor);
    }

    /**
     * Formata moeda conforme o Locale.
     * Evite montar símbolo manualmente; o NumberFormat cuida de separadores e símbolo.
     */
    public static String formatarMoeda(double valor, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(valor);
    }

    /**
     * Formata data/hora em estilo local padrão.
     * Se precisar fuso horário, considere usar ZonedDateTime.
     */
    public static String formatarDataHora(LocalDateTime dateTime, Locale locale) {
        DateTimeFormatter fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
        return fmt.format(dateTime);
    }
}