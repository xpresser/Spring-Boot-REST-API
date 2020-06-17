package com.metodi.projectapp.entities;

import com.metodi.projectapp.controllers.dtos.WorkLogRequestDTO;
import com.metodi.projectapp.controllers.dtos.WorkLogResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "worklogs")
@Getter @Setter @NoArgsConstructor
public class WorkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "task_id")
    private long taskID;

    @Column(name = "user_id")
    private long userID;

    @Column(name = "hours_spent")
    private int hoursSpent;

    @Column(name = "date_worked")
    private Date dateWorked;

    public void setWorkLogRequestDtoParameters(long taskID, User loggedUser,
                                               WorkLogRequestDTO workLogRequestDTO,
                                               WorkLog workLog) {
        workLog.setTaskID(taskID);
        workLog.setUserID(loggedUser.getId());
        workLog.setHoursSpent(workLogRequestDTO.getHoursSpent());
        workLog.setDateWorked(workLogRequestDTO.getDateWorked());
    }

    public void setWorkLogResponseDtoParameters(long taskID, User loggedUser,
                                                WorkLogResponseDTO workLogResponseDTO,
                                                WorkLog workLog) {
        workLog.setTaskID(taskID);
        workLog.setUserID(loggedUser.getId());
        workLog.setHoursSpent(workLogResponseDTO.getHoursSpent());
        workLog.setDateWorked(workLogResponseDTO.getDateWorked());
    }
}
