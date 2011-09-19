package org.slim3.json.test.issue_protobuf;

import org.junit.Assert;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

public class CharacterModelTest extends AppEngineTestCase {
    @Test
    public void test() throws Exception{
        CharacterModel m = new CharacterModel();
        m.setCharacterValue('c');
        Datastore.put(m);
        
        CharacterModel m2 = Datastore.get(CharacterModelMeta.get(), m.getKey());
        Assert.assertEquals('c', m2.getCharacterValue().charValue());
    }
}
