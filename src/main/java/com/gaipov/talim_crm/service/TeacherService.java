package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.AttendanceDto;
import com.gaipov.talim_crm.dto.GradeDto;
import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.entity.AttendanceEntity;
import com.gaipov.talim_crm.entity.GradeEntity;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.entity.TeacherEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.AttendanceRepository;
import com.gaipov.talim_crm.repository.GradeRepository;
import com.gaipov.talim_crm.repository.GroupRepo;
import com.gaipov.talim_crm.repository.StudentRepo;
import com.gaipov.talim_crm.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepo teacherRepo;
    private final StudentRepo studentRepo;
    private final GroupRepo groupRepo;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TeacherDto createTeacher(TeacherDto teacherDto) {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFullName(teacherDto.getFullName());
        teacherEntity.setPhoneNum(teacherDto.getPhoneNum());
        teacherEntity.setLevelOfKnowledge(teacherDto.getLevelOfKnowledge());
        teacherEntity.setUsername(teacherDto.getUsername());
        teacherEntity.setPassword(passwordEncoder.encode(teacherDto.getPassword()));
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

    public List<TeacherDto> getAllTeachers() {
        return teacherRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public String quitTeacher(Long teacherId) {
        TeacherEntity foundTeacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new NotFoundExp("Teacher not found"));

        foundTeacher.setDeleted_at(LocalDate.now());
        teacherRepo.save(foundTeacher);

        return "Successfully deleted";
    }

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
        
        if (!passwordEncoder.matches(password, teacher.getPassword())) {
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
        List<GroupEntity> teacherGroups = groupRepo.findByTeacherId(teacherId);
        
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
                existingAttendance.setIsPresent(dto.getIsPresent());
                existingAttendance.setNotes(dto.getNotes());
                attendanceRepository.save(existingAttendance);
            } else {
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

    public Map<String, Object> getWeeklyStatistics(Long teacherId) {
        Map<String, Object> stats = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<GroupEntity> teacherGroups = groupRepo.findByTeacherId(teacherId);
        List<StudentEntity> allStudents = teacherGroups.stream()
                .flatMap(g -> g.getListOfStudents().stream())
                .distinct()
                .collect(Collectors.toList());

        List<AttendanceEntity> weekAttendance = attendanceRepository.findAll().stream()
                .filter(a -> a.getCreatedByTeacherId() != null && a.getCreatedByTeacherId().equals(teacherId))
                .filter(a -> a.getAttendanceDate() != null)
                .filter(a -> !a.getAttendanceDate().isBefore(startOfWeek) && !a.getAttendanceDate().isAfter(endOfWeek))
                .collect(Collectors.toList());

        long presentCount = weekAttendance.stream().filter(AttendanceEntity::getIsPresent).count();
        long absentCount = weekAttendance.stream().filter(a -> !a.getIsPresent()).count();
        double attendanceRate = weekAttendance.isEmpty() ? 0 : (presentCount * 100.0 / weekAttendance.size());

        stats.put("totalStudents", allStudents.size());
        stats.put("totalGroups", teacherGroups.size());
        stats.put("presentCount", presentCount);
        stats.put("absentCount", absentCount);
        stats.put("attendanceRate", Math.round(attendanceRate * 10) / 10.0);
        stats.put("weekStart", startOfWeek.toString());
        stats.put("weekEnd", endOfWeek.toString());

        Map<String, Long> dailyPresent = new HashMap<>();
        Map<String, Long> dailyAbsent = new HashMap<>();
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            long present = weekAttendance.stream()
                    .filter(a -> a.getAttendanceDate().equals(currentDate) && a.getIsPresent())
                    .count();
            long absent = weekAttendance.stream()
                    .filter(a -> a.getAttendanceDate().equals(currentDate) && !a.getIsPresent())
                    .count();
            dailyPresent.put(date.getDayOfWeek().name(), present);
            dailyAbsent.put(date.getDayOfWeek().name(), absent);
        }
        stats.put("dailyPresent", dailyPresent);
        stats.put("dailyAbsent", dailyAbsent);

        return stats;
    }

    public Map<String, Object> getMonthlyStatistics(Long teacherId) {
        Map<String, Object> stats = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        List<GroupEntity> teacherGroups = groupRepo.findByTeacherId(teacherId);
        List<StudentEntity> allStudents = teacherGroups.stream()
                .flatMap(g -> g.getListOfStudents().stream())
                .distinct()
                .collect(Collectors.toList());

        List<AttendanceEntity> monthAttendance = attendanceRepository.findAll().stream()
                .filter(a -> a.getCreatedByTeacherId() != null && a.getCreatedByTeacherId().equals(teacherId))
                .filter(a -> a.getAttendanceDate() != null)
                .filter(a -> !a.getAttendanceDate().isBefore(startOfMonth) && !a.getAttendanceDate().isAfter(endOfMonth))
                .collect(Collectors.toList());

        long presentCount = monthAttendance.stream().filter(AttendanceEntity::getIsPresent).count();
        long absentCount = monthAttendance.stream().filter(a -> !a.getIsPresent()).count();
        double attendanceRate = monthAttendance.isEmpty() ? 0 : (presentCount * 100.0 / monthAttendance.size());

        stats.put("totalStudents", allStudents.size());
        stats.put("totalGroups", teacherGroups.size());
        stats.put("presentCount", presentCount);
        stats.put("absentCount", absentCount);
        stats.put("attendanceRate", Math.round(attendanceRate * 10) / 10.0);
        stats.put("monthStart", startOfMonth.toString());
        stats.put("monthEnd", endOfMonth.toString());

        Map<String, Long> weeklyPresent = new HashMap<>();
        Map<String, Long> weeklyAbsent = new HashMap<>();
        int weekNum = 1;
        LocalDate weekStart = startOfMonth;
        while (!weekStart.isAfter(endOfMonth)) {
            LocalDate weekEnd = weekStart.plusDays(6).isAfter(endOfMonth) ? endOfMonth : weekStart.plusDays(6);
            final LocalDate ws = weekStart;
            final LocalDate we = weekEnd;
            long present = monthAttendance.stream()
                    .filter(a -> !a.getAttendanceDate().isBefore(ws) && !a.getAttendanceDate().isAfter(we) && a.getIsPresent())
                    .count();
            long absent = monthAttendance.stream()
                    .filter(a -> !a.getAttendanceDate().isBefore(ws) && !a.getAttendanceDate().isAfter(we) && !a.getIsPresent())
                    .count();
            weeklyPresent.put("Week " + weekNum, present);
            weeklyAbsent.put("Week " + weekNum, absent);
            weekStart = weekStart.plusDays(7);
            weekNum++;
        }
        stats.put("weeklyPresent", weeklyPresent);
        stats.put("weeklyAbsent", weeklyAbsent);

        return stats;
    }

    // Grade management methods
    public void saveGrades(List<GradeDto> gradeData, Long teacherId) {
        for (GradeDto dto : gradeData) {
            GroupEntity group = groupRepo.findById(dto.getGroupId())
                    .orElseThrow(() -> new NotFoundExp("Group not found"));
            
            if (group.getTeacher() == null || !group.getTeacher().getId().equals(teacherId)) {
                throw new NotFoundExp("Group not found or access denied");
            }
            
            Optional<GradeEntity> existingGrade = gradeRepository
                    .findByStudentIdAndGroupIdAndLessonDate(dto.getStudentId(), dto.getGroupId(), dto.getLessonDate());
            
            if (existingGrade.isPresent()) {
                GradeEntity grade = existingGrade.get();
                grade.setGradeValue(dto.getGradeValue());
                grade.setLessonTopic(dto.getLessonTopic());
                grade.setComment(dto.getComment());
                grade.setUpdatedAt(LocalDateTime.now());
                gradeRepository.save(grade);
            } else {
                GradeEntity grade = new GradeEntity();
                StudentEntity student = studentRepo.findById(dto.getStudentId())
                        .orElseThrow(() -> new NotFoundExp("Student not found"));
                
                grade.setStudent(student);
                grade.setGroup(group);
                grade.setGradeValue(dto.getGradeValue());
                grade.setLessonDate(dto.getLessonDate());
                grade.setLessonTopic(dto.getLessonTopic());
                grade.setComment(dto.getComment());
                grade.setCreatedAt(LocalDateTime.now());
                grade.setCreatedByTeacherId(teacherId);
                
                gradeRepository.save(grade);
            }
        }
    }

    public List<GradeDto> getStudentGrades(Long studentId, Long teacherId) {
        List<GroupEntity> teacherGroups = groupRepo.findByTeacherId(teacherId);
        boolean belongs = teacherGroups.stream()
                .anyMatch(g -> g.getListOfStudents().stream().anyMatch(s -> s.getId().equals(studentId)));
        if (!belongs) throw new NotFoundExp("Access denied");

        return gradeRepository.findByStudentIdOrderByLessonDateDesc(studentId)
                .stream()
                .map(this::gradeToDto)
                .collect(Collectors.toList());
    }

    public List<GradeDto> getGroupGrades(Long groupId, Long teacherId) {
        GroupEntity group = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group not found"));
        
        if (group.getTeacher() == null || !group.getTeacher().getId().equals(teacherId)) {
            throw new NotFoundExp("Access denied");
        }

        return gradeRepository.findByGroupIdOrderByLessonDateDesc(groupId)
                .stream()
                .map(this::gradeToDto)
                .collect(Collectors.toList());
    }

    private GradeDto gradeToDto(GradeEntity entity) {
        GradeDto dto = new GradeDto();
        dto.setId(entity.getId());
        if (entity.getStudent() != null) {
            dto.setStudentId(entity.getStudent().getId());
            dto.setStudentName(entity.getStudent().getFullName());
        }
        if (entity.getGroup() != null) {
            dto.setGroupId(entity.getGroup().getId());
            dto.setGroupName(entity.getGroup().getNameOfGroup());
        }
        dto.setGradeValue(entity.getGradeValue());
        dto.setLessonDate(entity.getLessonDate());
        dto.setLessonTopic(entity.getLessonTopic());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedByTeacherId(entity.getCreatedByTeacherId());
        return dto;
    }
}
