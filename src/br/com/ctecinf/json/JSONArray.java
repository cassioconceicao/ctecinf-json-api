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

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Cássio Conceição
 * @param <T>
 * @since 31/05/2021
 * @version 2203
 * @see http://ctecinf.com.br/
 */
public class JSONArray<T extends Object> extends ArrayList<T> {

    /**
     * Verifica se valor do campor é um objeto JSON
     *
     * @param index
     * @return boolean
     */
    public boolean isJSONObjectValue(int index) {
        return get(index) != null && get(index).getClass().isAssignableFrom(JSONObject.class);
    }

    /**
     * Verifica se valor do campor é um objeto JSON
     *
     * @param index
     * @return boolean
     */
    public boolean isStringValue(int index) {
        return get(index) != null && get(index).getClass().isAssignableFrom(String.class);
    }

    @Override
    public T get(int index) {
        return (T) super.get(index);
    }

    
    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
}
