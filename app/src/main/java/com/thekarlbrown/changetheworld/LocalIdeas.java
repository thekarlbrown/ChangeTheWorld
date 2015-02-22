package com.thekarlbrown.changetheworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
class to save drafts to downloads as drafts_USERNAME
 */
public class LocalIdeas {
    IdeaBlock ideaBlock;
    private boolean saveIdeaBlock(File dst,IdeaBlock ib) {
        boolean res = false;
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(dst));
            output.writeObject(ib);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
            res=false;
        }finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                res=false;
            }
        }
        return res;
    }

    @SuppressWarnings({ "unchecked" })
    public IdeaBlock loadIdeaBlock(File src) {
        ObjectInputStream input = null;
        IdeaBlock ideaBlock;
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            ideaBlock=(IdeaBlock) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            ideaBlock=null;
        }finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return ideaBlock;
    }
    public boolean saveDraft(File src, String title,String idea,String author, int tup, int tdown, int number, int category, int subcategory)
    {
        ideaBlock =loadIdeaBlock(src);
       if(ideaBlock==null)
        {
            ideaBlock=new IdeaBlock();
       }
        ideaBlock.add(title,idea,author,tup,tdown,number,category,subcategory);
        return saveIdeaBlock(src,ideaBlock);
    }
}
