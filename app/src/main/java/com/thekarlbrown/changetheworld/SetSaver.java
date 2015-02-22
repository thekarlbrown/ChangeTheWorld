package com.thekarlbrown.changetheworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
currently un-used, but should enable local saving of sets
 */
//Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) to check mount
public class SetSaver {
    public boolean addToSet (File src,int item) {
        boolean res=false;
        ObjectInputStream input = null;

        Set<Integer> set=new HashSet<Integer>();
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            set = (Set<Integer>) input.readObject();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        set.add(item);
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(src));
            output.writeObject(set);
            res=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }

    public int[] getArrayFromSet (File src) {
        boolean res = false;
        ObjectInputStream input = null;

        Set<Integer> set = new HashSet<Integer>();
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            set = (Set<Integer>) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        int size=set.size();
        if(size==0)
        {
            return new int[]{-1};
        }
        int[]array=new int[size];
        int index=0;
        for( Integer i : set ) {
            array[index++] = i; //note the autounboxing here
        }
        return array;
    }
    public boolean setContains (File src,int target) {
        boolean res = false;
        ObjectInputStream input = null;

        Set<Integer> set = new HashSet<Integer>();
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            set = (Set<Integer>) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(set.size()==0)
        {
            return false;
        }
        if(set.contains(target))
        {
            return true;
        }
        return false;
    }
    public boolean removeFromSet (File src,int item) {
        boolean res=false;
        ObjectInputStream input = null;

        Set<Integer> set=new HashSet<Integer>();
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            set = (Set<Integer>) input.readObject();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        set.remove(item);
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(src));
            output.writeObject(set);
            res=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }
}
