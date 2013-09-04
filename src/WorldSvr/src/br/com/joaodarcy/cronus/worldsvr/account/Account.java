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

package br.com.joaodarcy.cronus.worldsvr.account;

import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import br.com.joaodarcy.cronus.worldsvr.item.CashItem;
import br.com.joaodarcy.cronus.worldsvr.item.ItemDuration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Account {
    private long id;
    private UInt32 cash = UInt32.ZERO;
    
    private UInt32 transactionId = UInt32.ZERO; // FIXME
    private final List<CashItem> cashItemList;

    public Account() {
        this.cash = UInt32.ZERO;
        this.cashItemList = new ArrayList<>();
    }

    public void addCashItem(Item item){
        addCashItem(item, ItemDuration.NO_DURATION);
    }
    
    public void addCashItem(Item item, ItemDuration duration){
        if(item == null){
            throw new IllegalArgumentException("Não é possível inserir um item nulo no inventário cash.");
        }
        if(duration == null){
            throw new IllegalArgumentException("Não é possível inserir um item com duração nula no inventário cash.");
        }        
        synchronized(cashItemList){
            transactionId = transactionId.increment();
            cashItemList.add(new CashItem(transactionId, item, duration));
        }
    }
    
    public CashItem receiveCashItem(UInt32 transactionId){
        if(transactionId == null){
            throw new IllegalArgumentException("Não é possível receber o item cash de uma transação nula");
        }
        synchronized(cashItemList){
            Iterator<CashItem> cashItemItr = cashItemList.iterator();
            
            while(cashItemItr.hasNext()){
                CashItem cashItem = cashItemItr.next();
                if(cashItem.getTransactionId().equals(transactionId)){
                    cashItemList.remove(cashItem);
                    return cashItem;
                }
            }            
        }
        return null;
    }
    
    public List<CashItem> getCashItemList() {
        return cashItemList;
    }
    
    public UInt32 getCash() {
        return cash;
    }

    public void setCash(UInt32 cash) {
        this.cash = cash;
    }        

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }    
}
