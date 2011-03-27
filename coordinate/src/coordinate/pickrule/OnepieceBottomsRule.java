/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package coordinate.pickrule;

import coordinate.enums.Category;
import coordinate.enums.Variety;
import coordinate.model.Item;

/**
 * The {@link Rule} for one-piece and bottoms.
 * 
 * @author higayasuo
 * 
 */
public class OnepieceBottomsRule extends AbstractRule {

    @Override
    protected boolean doIsSuitable(Item first, Item second)
            throws NullPointerException, IllegalArgumentException {
        if (first.getCategory() != Category.ONEPIECE) {
            throw new IllegalArgumentException(
                "The first item must be a one-piece, but the actual one is "
                    + first.getCategory());
        }
        if (second.getCategory() != Category.BOTTOMS) {
            throw new IllegalArgumentException(
                "The second item must be a bottoms, but the actual one is "
                    + second.getCategory());
        }
        return second.getVariety() != Variety.SKIRT;
    }
}