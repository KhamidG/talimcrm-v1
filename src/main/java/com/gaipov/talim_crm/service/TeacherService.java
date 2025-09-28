package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.AttendanceDto;
import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.entity.AttendanceEntity;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.entity.TeacherEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.AttendanceRepository;
import com.gaipov.talim_crm.repository.GroupRepo;
import com.gaipov.talim_crm.repository.StudentRepo;
import com.gaipov.talim_crm.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepo teacherRepo;
    private final StudentRepo studentRepo;
    private final GroupRepo groupRepo;
    private final AttendanceRepository attendanceRepository;

    public TeacherDto createTeacher(TeacherDto teacherDto) {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFullName(teacherDto.getFullName());
        teacherEntity.setPhoneNum(teacherDto.getPhoneNum());
        teacherEntity.setLevelOfKnowledge(teacherDto.getLevelOfKnowledge());
        teacherEntity.setUsername(teacherDto.getUsername());
        teacherEntity.setPassword(teacherDto.getPassword()); // In production, this should be hashed
        teacherEntity.setRoles(UserRole.TEACHER);
        teacherEntity.setCreated_at(LocalDate.now());

        teacherRepo.save(teacherEntity);
        teacherDto.setId(teacherEntity.getId());

        return teacherDto;
    }

    public List<TeacherDto> listOfDto() {
        return teacherRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TeacherDto> findByNameOfTeacher(String name) {
        List<TeacherEntity> optional = teacherRepo.findByFullName(name);

        if (optional.isEmpty()) {
            throw new NotFoundExp("Teacher not found");
        }
        return optional.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // All teachers list
    public List<TeacherDto> getAllTeachers() {
        return teacherRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // delete teacher
    public String quitTeacher(Long teacherId) {
        TeacherEntity foundTeacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new NotFoundExp("Teacher not found"));

        foundTeacher.setDeleted_at(LocalDate.now());
        teacherRepo.save(foundTeacher);

        return "Successfully deleted";
    }

    // Teacher go to leave, not deleted
    public String onLeave(Long teacherId) {
        TeacherEntity foundTeacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new NotFoundExp("Teacher not found"));

        foundTeacher.setOn_leave_time(LocalDate.now());
        teacherRepo.save(foundTeacher);

        return "Successfully go to leave";
    }

    private TeacherDto toDto(TeacherEntity entity) {
        TeacherDto dto = new TeacherDto();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setPhoneNum(entity.getPhoneNum());
        dto.setRoles(entity.getRoles());
        dto.setLevelOfKnowledge(entity.getLevelOfKnowledge());
        dto.setUsername(entity.getUsername());
        dto.setCreated_at(entity.getCreated_at());

        return dto;
    }

    public TeacherDto authenticate(String username, String password) {
        TeacherEntity teacher = teacherRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundExp("Teacher not found"));
        
        if (!password.equals(teacher.getPassword())) {
            throw new NotFoundExp("Invalid password");
        }
        
        return toDto(teacher);
    }

    public TeacherDto getTeacherById(Long id) {
        TeacherEntity teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new NotFoundExp("Teacher not found"));
        return toDto(teacher);
    }

    public List<StudentDto> getTeacherStudents(Long teacherId) {
        // Get all groups assigned to this teacher
        List<GroupEntity> teacherGroups = groupRepo.findByTeacherId(teacherId);
        
        // Get all students from these groups
        return teacherGroups.stream()
                .flatMap(group -> group.getListOfStudents().stream())
                .map(this::studentToDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getTeacherGroups(Long teacherId) {
        return groupRepo.findByTeacherId(teacherId).stream()
                .map(this::groupToDto)
                .collect(Collectors.toList());
    }

    private StudentDto studentToDto(StudentEntity entity) {
        StudentDto dto = new StudentDto();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setPhoneNum(entity.getPhoneNum());
        dto.setParentName(entity.getParentName());
        dto.setParentPhone(entity.getParentPhone());
        dto.setStatus(entity.getStatus());
        dto.setCreated_at(entity.getCreated_at());

        GroupEntity group = entity.getGroup();
        if (group != null) {
            GroupDto groupDto = new GroupDto();
            groupDto.setId(group.getId());
            groupDto.setNameOfGroup(group.getNameOfGroup());
            groupDto.setTypeOfGroup(group.getTypeOfGroup());
            groupDto.setCurrentStudents(group.getCurrentStudents());
            groupDto.setMaxStudents(group.getMaxStudents());
            groupDto.setLessonStartTime(group.getLessonStartTime());
            groupDto.setLessonEndTime(group.getLessonEndTime());
            groupDto.setLessonDays(group.getLessonDays());
            if (group.getTeacher() != null) {
                groupDto.setTeacherFullName(group.getTeacher().getFullName());
                groupDto.setTeacherId(group.getTeacher().getId());
            }
            dto.setGroup(groupDto);
        }
        return dto;
    }

    private GroupDto groupToDto(GroupEntity entity) {
        GroupDto dto = new GroupDto();
        dto.setId(entity.getId());
        dto.setNameOfGroup(entity.getNameOfGroup());
        dto.setTypeOfGroup(entity.getTypeOfGroup());
        dto.setCurrentStudents(entity.getCurrentStudents());
        dto.setMaxStudents(entity.getMaxStudents());
        dto.setLessonStartTime(entity.getLessonStartTime());
        dto.setLessonEndTime(entity.getLessonEndTime());
        dto.setLessonDays(entity.getLessonDays());
        if (entity.getTeacher() != null) {
            dto.setTeacherFullName(entity.getTeacher().getFullName());
        }
        return dto;
    }

    public List<StudentDto> getGroupStudentsForTeacher(Long groupId, Long teacherId) {
        GroupEntity group = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group not found"));
        if (group.getTeacher() == null || !group.getTeacher().getId().equals(teacherId)) {
            throw new NotFoundExp("Access denied");
        }
        return group.getListOfStudents().stream().map(this::studentToDto).collect(Collectors.toList());
    }

    public List<StudentDto> getGroupStudents(Long groupId, Long teacherId) {
        // Verify that the group belongs to the teacher
        GroupEntity group = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group not found"));
        

        if (group.getTeacher() == null || !group.getTeacher().getId().equals(teacherId)) {
            throw new NotFoundExp("Group not found or access denied");
        }
        
        List<StudentEntity> students = group.getListOfStudents();
        return students.stream()
                .map(this::studentToDto)
                .collect(Collectors.toList());
    }

    public void saveAttendance(List<AttendanceDto> attendanceData, Long teacherId) {
        for (AttendanceDto dto : attendanceData) {
            GroupEntity group = groupRepo.findById(dto.getGroupId())
                    .orElseThrow(() -> new NotFoundExp("Group not found"));
            
            if (group.getTeacher() == null || !group.getTeacher().getId().equals(teacherId)) {
                throw new NotFoundExp("Group not found or access denied");
            }
            
            AttendanceEntity existingAttendance = attendanceRepository
                    .findByStudentIdAndGroupIdAndAttendanceDate(dto.getStudentId(), dto.getGroupId(), dto.getAttendanceDate())
                    .orElse(null);
            
            if (existingAttendance != null) {
                // Update existing attendance
                existingAttendance.setIsPresent(dto.getIsPresent());
                existingAttendance.setNotes(dto.getNotes());
                attendanceRepository.save(existingAttendance);
            } else {
                // Create new attendance record
                AttendanceEntity attendance = new AttendanceEntity();
                StudentEntity student = studentRepo.findById(dto.getStudentId())
                        .orElseThrow(() -> new NotFoundExp("Student not found"));
                
                attendance.setStudent(student);
                attendance.setGroup(group);
                attendance.setAttendanceDate(dto.getAttendanceDate());
                attendance.setIsPresent(dto.getIsPresent());
                attendance.setNotes(dto.getNotes());
                attendance.setCreatedAt(LocalDateTime.now());
                attendance.setCreatedByTeacherId(teacherId);
                
                attendanceRepository.save(attendance);
            }
        }
    }

    public List<AttendanceEntity> getStudentAttendance(Long studentId, Long teacherId) {
        // Optionally verify that the student is in any group of this teacher
        List<GroupEntity> teacherGroups = groupRepo.findByTeacherId(teacherId);
        boolean belongs = teacherGroups.stream()
                .anyMatch(g -> g.getListOfStudents().stream().anyMatch(s -> s.getId().equals(studentId)));
        if (!belongs) throw new NotFoundExp("Access denied");

        return attendanceRepository.findByStudentIdOrderByAttendanceDateDesc(studentId);
    }

    public List<AttendanceDto> getStudentAttendanceHistory(Long studentId) {
        return attendanceRepository.findByStudentIdOrderByAttendanceDateDesc(studentId)
                .stream()
                .map(this::attendanceToDto)
                .collect(Collectors.toList());
    }

    private AttendanceDto attendanceToDto(AttendanceEntity entity) {
        AttendanceDto dto = new AttendanceDto();
        dto.setId(entity.getId());
        if (entity.getStudent() != null) {
            dto.setStudentId(entity.getStudent().getId());
            dto.setStudentName(entity.getStudent().getFullName());
        }
        if (entity.getGroup() != null) {
            dto.setGroupId(entity.getGroup().getId());
            dto.setGroupName(entity.getGroup().getNameOfGroup());
        }
        dto.setAttendanceDate(entity.getAttendanceDate());
        dto.setIsPresent(entity.getIsPresent());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedByTeacherId(entity.getCreatedByTeacherId());
        return dto;
    }
}
