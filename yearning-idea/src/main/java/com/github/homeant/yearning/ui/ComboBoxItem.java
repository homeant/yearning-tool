package com.github.homeant.yearning.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComboBoxItem {
    private String name;

    private String value;

    @Override
    public String toString() {
        return name;
    }
}
