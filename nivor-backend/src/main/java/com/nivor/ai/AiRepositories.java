package com.nivor.ai;import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;
interface AiConversationRepository extends JpaRepository<AiConversation,Long>{List<AiConversation> findAllByUser_IdOrderByUpdatedAtDesc(Long id);Optional<AiConversation> findByIdAndUser_Id(Long id,Long userId);}
interface AiMessageRepository extends JpaRepository<AiMessage,Long>{List<AiMessage> findAllByConversation_IdOrderByCreatedAtAsc(Long id);List<AiMessage> findAllByConversation_IdOrderByCreatedAtDesc(Long id,org.springframework.data.domain.Pageable page);}
interface AiActionRepository extends JpaRepository<AiAction,Long>{Optional<AiAction> findByIdAndUser_Id(Long id,Long userId);void deleteAllByConversation_IdAndUser_Id(Long conversationId,Long userId);}
interface AiAccessRepository extends JpaRepository<AiAccessPreference,Long>{List<AiAccessPreference> findAllByUser_Id(Long id);Optional<AiAccessPreference> findByUser_IdAndCategory(Long id,AiContextCategory category);}
interface AiMemoryRepository extends JpaRepository<AiMemory,Long>{List<AiMemory> findAllByUser_IdOrderByUpdatedAtDesc(Long id);Optional<AiMemory> findByIdAndUser_Id(Long id,Long userId);}
