package com.example.nftcam.api.entity.material;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.nftcam.api.entity.material.QMaterial.material;

@Repository
@RequiredArgsConstructor
public class MaterialRepositoryCustomImpl implements MaterialRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Material> findAllByUserIdWithPaging(Long userId, Long cursorId, Pageable pageable) {
        JPAQuery<Material> query = jpaQueryFactory
                .selectFrom(material)
                .where(
                        ltMaterialId(cursorId)
                )
                .orderBy(material.createdAt.desc())
                .limit(pageable.getPageSize());

        return query.fetch();
    }

    private BooleanExpression ltMaterialId(Long cursorId) {
        return cursorId == null ?
                null :
                material.id.lt(cursorId);
    }

}
