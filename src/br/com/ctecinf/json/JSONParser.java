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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Cássio Conceição
 * @since 28/03/2022
 * @version 2203
 * @see http://ctecinf.com.br/
 */
public class JSONParser {

    /**
     * Cria objeto <i>JSON</i> à partir de <i>String</i> JSON
     *
     * @param <T>
     * @param file
     * @return JSONObject ou JSONArray
     * @throws br.com.ctecinf.json.JSONException
     */
    public static <T> T toObject(File file) throws JSONException {

        if (!file.exists()) {
            throw new JSONException("File not found.");
        }

        StringBuilder str = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                str.append(line);
            }
        } catch (IOException ex) {
            throw new JSONException(ex);
        }

        return toObject(str.toString());
    }

    /**
     * Cria objeto <i>JSON</i> à partir de <i>String</i> JSON
     *
     * @param <T>
     * @param str
     * @return JSONObject ou JSONArray
     * @throws br.com.ctecinf.json.JSONException
     */
    public static <T> T toObject(String str) throws JSONException {

        str = str.replace("\\n", "$newline$").replace("\n", "$newline$").replaceAll("[ ]{2,}", " ").trim();

        if (str.startsWith("[")) {

            String value = getContent(str.substring(1), "[", ']');
            value = value.substring(0, value.length() - 1);

            JSONArray list = new JSONArray();

            for (String s : value.split("\\} ?\\, ?\\{")) {
                JSONObject j = new JSONObject();
                stringToJSON(s.replace("{", "").replace("}", "").trim(), j);
                list.add(j);
            }

            return (T) list;

        } else {

            JSONObject json = new JSONObject();
            stringToJSON(str, json);

            return (T) json;
        }
    }

    /**
     * Passa para objeto JSON a string
     *
     * @param str
     * @param json
     */
    private static void stringToJSON(String str, JSONObject json) throws JSONException {

        int pos = 0;
        String[] keyValue = str.split(":");

        for (int i = 0; i < keyValue[0].length(); i++) {
            if (Character.isLetterOrDigit(keyValue[0].charAt(i))) {
                pos = i;
                break;
            }
        }

        String key = keyValue[0].substring(pos);
        pos += keyValue[0].substring(pos).length();

        for (int i = key.length() - 1; i > 0; i--) {
            if (!Character.isLetterOrDigit(key.charAt(i)) && key.charAt(i) != '_') {
                pos++;
                key = key.substring(0, i);
            }
        }

        String value;

        if (keyValue.length > 1 && !key.isEmpty()) {

            if (keyValue[1].startsWith(" ")) {
                pos++;
                value = keyValue[1].substring(1);
            } else {
                value = keyValue[1];
            }

            if (value.startsWith("[")) {

                pos++;

                value = getContent(str.substring(pos), "[", ']');

                pos += value.length();

                value = value.substring(0, value.length() - 1);

                JSONArray list = new JSONArray();

                String[] split = value.split("\\} ?\\, ?\\{");
                if (split.length == 1) {
                    list.addAll(Arrays.asList(value.split(",")));
                } else {
                    for (String s : split) {
                        JSONObject j = new JSONObject();
                        stringToJSON(s.replace("{", "").replace("}", "").trim(), j);
                        list.add(j);
                    }
                }

                json.put(key, list);

            } else if (value.startsWith("{")) {

                pos++;

                value = getContent(str.substring(pos), "{", '}');

                pos += value.length();

                value = value.substring(0, value.length() - 1);

                JSONObject j = new JSONObject();
                stringToJSON(value, j);
                json.put(key, j);

            } else if (value.startsWith("\"")) {

                pos++;
                value = str.substring(pos).split("\"")[0].replace("$newline$", "\\n").trim();
                pos += value.length() + 1;

                json.put(key, value);

            } else {
                throw new JSONException("Erro na formatação do arquivo JSON.");
            }

            if (pos < str.length()) {
                stringToJSON(str.substring(pos), json);
            }
        }
    }

    /**
     * Pega conteúdo de uma <i>String</i> entre parâmetros <i>open</i> e
     * <i>close</i>
     *
     * @param str Linha para extraír conteúdo
     * @param open Exemplos: "[", "{", ...
     * @param close Exemplos: ']', '}', ...
     * @return
     */
    private static String getContent(String str, String open, char close) {

        StringBuilder sb = new StringBuilder();

        for (char c : str.toCharArray()) {

            sb.append(c);

            if (c == close) {
                break;
            }
        }

        if (sb.toString().contains(open)) {
            sb.append(getContent(str.replace(sb.toString(), ""), open, close));
        }

        return sb.toString();
    }
}
