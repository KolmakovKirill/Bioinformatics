package project.urfu.vk.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VkMemberResponse
{
    private Long id;
    private Integer age;
    private String first_name;
    private String last_name;
    private VkCityResponse city;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setBdate(String dateString)
    {
        if (dateString != null)
        {
            SimpleDateFormat dateFormat;
            if (dateString.length() == 10) {
                dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            } else if (dateString.length() == 5) {
                this.age = null;
                return; // Неверный формат даты
            } else {
                this.age = null;
                return; // Неверный формат даты
            }

            try {
                Date date = dateFormat.parse(dateString);

                Calendar dob = Calendar.getInstance();
                dob.setTime(date);
                Calendar today = Calendar.getInstance();

                int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }

                this.age = age;
            } catch (ParseException e) {
                e.printStackTrace(); // Ошибка при парсинге даты

            }
        }
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public VkCityResponse getCity()
    {
        return city;
    }

    public void setCity(VkCityResponse city)
    {
        this.city = city;
    }
}
