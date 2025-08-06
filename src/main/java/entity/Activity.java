package entity;




public class Activity  {

   
    private String name;
    private String description;
    
   

    public Activity() {}

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
        
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}