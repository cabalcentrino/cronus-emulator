/*
 * Copyright (C) 2013 João Darcy Tinoco Sant´Anna Neto <jdtsncomp@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.joaodarcy.cronus.worldsvr.item;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public enum ItemPropertie {
                                                //DROP   TRADE  SELL   BOUND  STORE
    NORMAL                                       (true , true , true , false, true ),
    NAO_DROPA                                    (false, true , true , false, true ),
    NAO_NEGOCIA                                  (true , false, true , false, true ),
    LIGADO                                       (false, false, true , true , true ),
    NAO_VENDE                                    (true , true , false, false, true ),
    NAO_DROPA_NAO_VENDE                          (false, true , false, false, true ),
    NAO_DROPA_NAO_VENDE_NAO_NEGOCIA              (false, false, false, false, true ),
    LIGADO_NAO_VENDE                             (true , false, false, true , true ),
    NAO_ARMAZENA                                 (true , true , true , false, false),
    NAO_DROPA_NAO_ARMAZENA                       (false, true , true , false, false),
    NAO_ARMAZENA_NAO_NEGOCIA                     (true , false, true , false, false),
    LIGADO_AO_PERSONAGEM                         (false, false, true , true , false),
    NAO_VENDE_NAO_ARMAZENA                       (true , true , false, false, false),
    NAO_DROPA_NAO_VENDE_NAO_ARMAZENA             (false, true , false, false, false),
    NAO_VENDE_NAO_ARMAZENA_NAO_NEGOCIA           (true , false, false, false, false),
    LIGADO_AO_PERSONAGEM_NAO_VENDE               (false, false, false, true , false);
   
    private final boolean drop;
    private final boolean trade;
    private final boolean sell;
    private final boolean bound;
    private final boolean store;
            
    private ItemPropertie(boolean drop, boolean trade, boolean sell, boolean bound, boolean store) {
        this.drop = drop;
        this.trade = trade;
        this.sell = sell;
        this.bound = bound;
        this.store = store;
    }
    
    public boolean canDrop(){
        return drop;
    }
    
    public boolean canTrade(){
        return trade;
    }
    
    public boolean canSell(){
        return sell;
    }
    
    public boolean canStore(){
        return store;
    }

    public boolean isBound() {
        return bound;
    }    
}
