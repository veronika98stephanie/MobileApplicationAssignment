package veroNstella.rmit.assignment.model.attendee;

public abstract class AttendeeAbstract implements Attendee {
    private String attendeesId;
    private String name;

    AttendeeAbstract(String attendeesId, String name) {
        this.attendeesId = attendeesId;
        this.name = name;
    }

    public String getAttendeesId() {
        return attendeesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
