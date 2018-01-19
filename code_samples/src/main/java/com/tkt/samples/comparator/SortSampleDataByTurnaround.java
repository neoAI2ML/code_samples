package com.tkt.samples.comparator;

import java.util.Comparator;

import com.tkt.samples.example.SampleData;

public class SortSampleDataByTurnaround implements Comparator<SampleData> {

	@Override
	public int compare(SampleData o1, SampleData o2) {
		return (int) (o1.getTurnaround() - o2.getTurnaround());
	}
}
