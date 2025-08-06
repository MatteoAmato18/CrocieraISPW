package dao;

import entity.Staf;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StafDaoFile implements GenericDao<Staf> {

    private static final String FILE_PATH = "staf.json";
    private final Gson gson;
    private List<Staf> stafList;

    public StafDaoFile() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting().create();
        stafList = load();
    }
    private List<Staf> load() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(f)) {
            Type t = new TypeToken<List<Staf>>(){}.getType();
            List<Staf> l = gson.fromJson(r,t);
            return l != null ? l : new ArrayList<>();
        } catch (IOException e){ e.printStackTrace(); return new ArrayList<>(); }
    }
    private void save() { try (Writer w=new FileWriter(FILE_PATH)){ gson.toJson(stafList,w);}catch(IOException e){e.printStackTrace();}}

    @Override public void create(Staf s){
        if(read(s.getEmail())!=null) throw new IllegalArgumentException("Staf esiste: "+s.getEmail());
        stafList.add(s);
        save();
        }
    @Override public Staf read(Object...k){
    	String email=(String)k[0];
        return stafList.stream().filter(s->s.getEmail().equals(email)).findFirst().orElse(null);
        }
    @Override public void update(Staf s){ 
    	for(int i=0;i<stafList.size();i++) {
         if(stafList.get(i).getEmail().equals(s.getEmail()))
         {
        	 stafList.set(i,s);
         save();
         return;
         }}
        throw new IllegalArgumentException("Staf non trovato");}
   
    
    
    
    @Override public void delete(Object...k){
    	String email=(String)k[0];
        stafList.removeIf(s->s.getEmail().equals(email)); 
        save();
        }
    @Override public List<Staf> readAll(){ 
    	return new ArrayList<>(stafList);
    	}
}