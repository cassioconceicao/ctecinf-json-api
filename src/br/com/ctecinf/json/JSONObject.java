/*
 * Copyright (c) 2022, ctecinf.com.br
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package br.com.ctecinf.json;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Cássio Conceição
 * @since 31/05/2021
 * @version 2203
 * @see http://ctecinf.com.br/
 */
public class JSONObject extends LinkedHashMap<String, Object> {

    /**
     * Verifica se valor do campor é um objeto JSONArray
     *
     * @param key Nome do campo
     * @return boolean
     */
    public boolean isJSONArrayValue(String key) {
        return get(key) != null && get(key).getClass().isAssignableFrom(JSONArray.class);
    }

    /**
     * Verifica se valor do campor é um objeto JSON
     *
     * @param key Nome do campo
     * @return boolean
     */
    public boolean isJSONObjectValue(String key) {
        return get(key) != null && get(key).getClass().isAssignableFrom(JSONObject.class);
    }
    
    /**
     * Recupera o valor <i>Object</i> do campo
     *
     * @param <T>
     * @param key Nome do campo
     * @return T
     */
    public <T> T getValue(String key) {
        return (T) get(key);
    }

    /**
     * Recupera o valor <i>String</i> do campo
     *
     * @param key Nome do campo
     * @return String
     */
    public String getStringValue(String key) {
        return getValue(key) == null ? null : getValue(key).toString();
    }

    /**
     * Valor da coluna
     *
     * @param key
     * @return Calendar
     */
    public Calendar getCalendarValue(String key) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")), new Locale("pt", "BR"));
        if (getValue(key) != null) {
            calendar.setTime(getValue(key));
        }
        return calendar;
    }

    /**
     * Valor da coluna
     *
     * @param key
     * @return Number
     */
    public Number getNumberValue(String key) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        numberFormat.setMinimumFractionDigits(2);
        try {
            return getStringValue(key) == null ? null : numberFormat.parse(getStringValue(key));
        } catch (ParseException ex) {
            System.err.println(key.toUpperCase() + ": " + ex.getMessage());
            return null;
        }
    }

    /**
     * Recupera o valor <i>Boolean</i> do campo
     *
     * @param key Nome do campo
     * @return boolean
     */
    public boolean getBooleanValue(String key) {
        return this.getStringValue(key).equalsIgnoreCase("success") || this.getStringValue(key).equalsIgnoreCase("true") || this.getStringValue(key).equalsIgnoreCase("1") || this.getStringValue(key).equalsIgnoreCase("yes") || this.getStringValue(key).equalsIgnoreCase("on");
    }

    /**
     * Recupera o valor <i>JSON</i> do campo
     *
     * @param key Nome do campo
     * @return JSONObject
     */
    public JSONObject getJSONObjectValue(String key) {
        return getValue(key);
    }

    /**
     * Recupera o valor <i>JSONArray</i> do campo
     *
     * @param key Nome do campo
     * @return JSONArray
     */
    public JSONArray getJSONArrayValue(String key) {
        return getValue(key);
    }

    /**
     *
     * @param data
     * @return String
     */
    private String parserToString(JSONObject data) {
        StringBuilder sb = new StringBuilder("{");
        data.entrySet().stream().forEach((entry) -> {
            Object value = entry.getValue();
            if (value != null && (value.getClass().isAssignableFrom(JSONObject.class) || value.getClass().isAssignableFrom(JSONArray.class))) {
                sb.append("\"").append(entry.getKey()).append("\":").append(value).append(",");
            } else {
                sb.append("\"").append(entry.getKey()).append("\":\"").append(value == null ? "" : value.toString().replace("\n", "")).append("\",");
            }
        });
        return sb.substring(0, sb.length() - 1).concat("}");
    }

    @Override
    public String toString() {
        return parserToString(this);
    }
}
