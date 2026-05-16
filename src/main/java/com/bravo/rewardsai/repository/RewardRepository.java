package com.bravo.rewardsai.repository;

import com.bravo.rewardsai.entity.Reward;
import com.bravo.rewardsai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByUserAndIsUsedFalse(User user);
}
