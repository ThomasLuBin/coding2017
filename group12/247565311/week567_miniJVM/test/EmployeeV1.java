package week567_miniJVM.test;

public class EmployeeV1 {
    private int age;
    private String name;
    public EmployeeV1(int _age,String _name){
    	age = _age;
    	name = _name;
    }
    public void setAge(int _age){
    	age = _age;
    }
    public int getAge(){
    	return age;
    }
    public void setName(String _name){
    	name = _name;
    }
    public String getName(){
    	return name;
    }
}
