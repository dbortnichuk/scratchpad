package edu.dbortnichuk.java;

/**
 * Created by dbort on 01.07.2016.
 */
public class TestRunnable implements Runnable {

    private DTO dto;
    private String label;

    public TestRunnable(String label, DTO dto) {
        this.dto = dto;
        this.label = label;
    }

    public DTO getDto() {
        return dto;
    }

    public void run() {
        dto.setStrVal(label);
        System.out.println("State of dto : " + dto.getStrVal());
    }
}
