package mas15DEPQ;

public interface DEPQ {
  
  public Comparable inspectLeast();
  
  public Comparable inspectMost();

  public void add(Comparable c);
 
  public Comparable getLeast();  
    
  public Comparable getMost();
 
  public boolean isEmpty();
  
  public int size();
}
