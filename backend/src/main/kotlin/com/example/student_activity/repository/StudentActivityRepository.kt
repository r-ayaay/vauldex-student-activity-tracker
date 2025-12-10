package com.example.student_activity.repository

import com.example.student_activity.entity.StudentActivity
import com.example.student_activity.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface StudentActivityRepository : JpaRepository<StudentActivity, Long>, JpaSpecificationExecutor<StudentActivity>{
    fun findByStudent(student: User): List<StudentActivity>
}
