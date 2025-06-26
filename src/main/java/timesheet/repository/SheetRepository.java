package timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import timesheet.entity.Sheet;

import java.util.List;


public interface SheetRepository extends JpaRepository<Sheet, Long> {
    @Query(value = "SELECT * FROM sheet WHERE user_id = :userId", nativeQuery = true)
    List<Sheet> getListOfSheetById(@Param("userId") Long userId);



}
