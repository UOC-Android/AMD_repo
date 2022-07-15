package com.andresgqjob.todolist;


public class Grupo
{
    public int id;
    public String groupName;

    public Grupo()
    {
        super();
    }

    public Grupo(String groupName)
    {
        this.groupName = groupName;
    }

    public Grupo(int id) {
        this.id = id;
    }

    public Grupo(int id, String groupName)
    {
        this.id = id;
        this.groupName = groupName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    @Override
    public String toString()
    {
        return "Grupo{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grupo grupo = (Grupo) o;

        return id == grupo.id &&
                groupName.equals(grupo.groupName);
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + groupName.hashCode();
        return result;
    }
}


