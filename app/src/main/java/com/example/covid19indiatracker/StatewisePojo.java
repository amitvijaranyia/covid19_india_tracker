package com.example.covid19indiatracker;

public class StatewisePojo {
    String stateName;
    String confirmed, active, recovered, deceased, confirmedIncrease, activeIncrease, recoveredIncrease, deceasedIncrease;

    public StatewisePojo(String stateName, String confirmed, String active, String recovered,
                         String deceased, String confirmedIncrease, String activeIncrease,
                         String recoveredIncrease, String deceasedIncrease) {
        this.stateName = stateName;
        this.confirmed = confirmed;
        this.active = active;
        this.recovered = recovered;
        this.deceased = deceased;
        this.confirmedIncrease = confirmedIncrease;
        this.activeIncrease = activeIncrease;
        this.recoveredIncrease = recoveredIncrease;
        this.deceasedIncrease = deceasedIncrease;
    }
}
