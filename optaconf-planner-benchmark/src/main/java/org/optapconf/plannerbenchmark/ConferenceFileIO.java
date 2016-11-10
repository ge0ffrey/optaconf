/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optapconf.plannerbenchmark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.optaconf.domain.Conference;
import org.optaconf.domain.Room;
import org.optaconf.domain.Talk;
import org.optaconf.domain.Timeslot;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

public class ConferenceFileIO implements SolutionFileIO {

    @Override
    public String getInputFileExtension() {
        return "xlsx";
    }

    @Override
    public String getOutputFileExtension() {
        return "xlsx";
    }

    @Override
    public Solution read(File inputSolutionFile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(Solution solution, File outputSolutionFile) {
        Conference conference = (Conference) solution;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Conference");
        XSSFRow headerRow = sheet.createRow(0);
        int x = 1;
        Map<Room, Integer> roomXMap = new HashMap<>(conference.getRoomList().size());
        for (Room room : conference.getRoomList()) {
            XSSFCell cell = headerRow.createCell(x);
            cell.setCellValue(room.getName());
            roomXMap.put(room, x);
            x++;
        }
        int y = 1;
        Map<Timeslot, XSSFRow> timeslotRowMap = new HashMap<>(conference.getTimeslotList().size());
        for (Timeslot timeslot : conference.getTimeslotList()) {
            XSSFRow row = sheet.createRow(y);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(timeslot.getDay().getName() + " - " + timeslot.getName());
            timeslotRowMap.put(timeslot, row);
            y++;
        }
        for (Talk talk : conference.getTalkList()) {
            Timeslot timeslot = talk.getTimeslot();
            Room room = talk.getRoom();
            if (timeslot != null && room != null) {
                XSSFCell cell = timeslotRowMap.get(timeslot).createCell(roomXMap.get(room));
                cell.setCellValue(talk.getTitle());
            } else {
                XSSFCell unassignedCell = sheet.createRow(y).createCell(1);
                unassignedCell.setCellValue(talk.getTitle());
                y++;
            }
        }
        try {
            try (OutputStream out = new FileOutputStream(outputSolutionFile)) {
                workbook.write(out);
                workbook.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Problem writing outputSolutionFile (" + outputSolutionFile + ").", e);
        }
    }
}
