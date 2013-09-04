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

package br.com.joaodarcy.cronus.cabal.core.patterns;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class AbstractChain<ReturnType, ParameterType> implements Chain<ReturnType, ParameterType> {
    protected Chain<ReturnType, ParameterType> next;

    public AbstractChain() {
        this.next = null;
    }
    
    protected abstract boolean canHandle(ParameterType value);
    protected abstract ReturnType handleValue(ParameterType value);
    protected abstract ReturnType endOfChain();
    
    @Override
    public ReturnType handle(ParameterType value) {
        if(canHandle(value)){
            return handleValue(value);
        }else{
            if(next == null){
                return endOfChain();
            }else{
                return next.handle(value);
            }
        }
    }
    
    @Override
    public void setNext(Chain<ReturnType, ParameterType> next) {
        this.next = next;
    }
}
