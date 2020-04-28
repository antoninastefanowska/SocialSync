package com.antonina.migrationprocessor;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({
        "android.arch.persistence.room.ColumnInfo",
        "android.arch.persistence.room.Entity",
        "android.arch.persistence.room.ForeignKey",
        "android.arch.persistence.room.PrimaryKey"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class MigrationProcessor extends AbstractProcessor {
    private static TypeMirror STRING_TYPE;
    private static TypeMirror INTEGER_TYPE;
    private static TypeMirror LONG_TYPE;
    private static TypeMirror BOOLEAN_TYPE;
    private static TypeMirror DATE_TYPE;

    private Messager messager;
    private Filer filer;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        typeUtils = processingEnvironment.getTypeUtils();

        Elements elementUtils = processingEnvironment.getElementUtils();
        STRING_TYPE = elementUtils.getTypeElement("java.lang.String").asType();
        INTEGER_TYPE = elementUtils.getTypeElement("java.lang.Integer").asType();
        LONG_TYPE = elementUtils.getTypeElement("java.lang.Long").asType();
        BOOLEAN_TYPE = elementUtils.getTypeElement("java.lang.Boolean").asType();
        DATE_TYPE = elementUtils.getTypeElement("java.util.Date").asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        ClassName className = ClassName.get("android.arch.persistence.db", "SupportSQLiteDatabase");

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("runGeneratedQueries")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(className, "db");

        Collection<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(Entity.class);
        List<TypeElement> entityElements = new ArrayList<>(ElementFilter.typesIn(annotatedElements));
        List<IndexEntry> indices = new ArrayList<>();

        for (TypeElement type : entityElements) {
            StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            Entity entityAnnotation = type.getAnnotation(Entity.class);
            String tableName = entityAnnotation.tableName(), separator = "";

            queryBuilder.append("`");
            queryBuilder.append(tableName);
            queryBuilder.append("` (");

            List<? extends Element> fields = type.getEnclosedElements();
            for (Element field : fields) {
                ColumnInfo columnAnnotation = field.getAnnotation(ColumnInfo.class);
                if (field.getKind().isField() && columnAnnotation != null) {
                    queryBuilder.append(separator);
                    processColumn(field, entityAnnotation, columnAnnotation, queryBuilder, indices);
                    separator = ", ";
                }
            }
            ForeignKey[] foreignKeys = entityAnnotation.foreignKeys();
            for (ForeignKey foreignKey : foreignKeys) {
                queryBuilder.append(separator);
                processForeignKey(foreignKey, queryBuilder);
            }
            queryBuilder.append(")");
            methodBuilder.addStatement("db.execSQL($S)", queryBuilder.toString());
        }

        for (IndexEntry indexEntry : indices) {
            String query = processIndex(indexEntry);
            methodBuilder.addStatement("db.execSQL($S)", query);
        }

        MethodSpec method = methodBuilder.build();
        TypeSpec type = TypeSpec.classBuilder("MigrationQueryGenerator")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(method)
                .build();

        JavaFile file = JavaFile.builder("com.antonina.socialsynchro.common.database.migrations", type)
                .build();

        try {
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void processColumn(Element field, Entity entityAnnotation, ColumnInfo columnAnnotation, StringBuilder queryBuilder, List<IndexEntry> indices) {
        queryBuilder.append("`");
        queryBuilder.append(columnAnnotation.name());
        queryBuilder.append("` ");

        TypeMirror columnType = field.asType();
        TypeKind columnKind = columnType.getKind();
        if (columnKind.isPrimitive()) {
            if (columnKind == TypeKind.INT ||
                    columnKind == TypeKind.LONG ||
                    columnKind == TypeKind.BOOLEAN)
                queryBuilder.append("INTEGER NOT NULL");
        } else {
            if (typeUtils.isSameType(columnType, INTEGER_TYPE) ||
                    typeUtils.isSameType(columnType, LONG_TYPE) ||
                    typeUtils.isSameType(columnType, BOOLEAN_TYPE) ||
                    typeUtils.isSameType(columnType, DATE_TYPE))
                queryBuilder.append("INTEGER");
            else if (typeUtils.isSameType(columnType, STRING_TYPE))
                queryBuilder.append("TEXT");
        }

        PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
        if (primaryKeyAnnotation != null) {
            queryBuilder.append(" PRIMARY KEY");
            if (primaryKeyAnnotation.autoGenerate())
                queryBuilder.append(" AUTOINCREMENT");
        }
        if (columnAnnotation.index())
            indices.add(new IndexEntry(entityAnnotation, columnAnnotation));
    }

    private void processForeignKey(ForeignKey foreignKey, StringBuilder queryBuilder) {
        Entity parentAnnotation;
        try {
            parentAnnotation = (Entity) foreignKey.entity().getAnnotation(Entity.class);
        } catch (MirroredTypeException e) {
            DeclaredType declaredType = (DeclaredType)e.getTypeMirror();
            TypeElement parent = (TypeElement)declaredType.asElement();
            parentAnnotation = parent.getAnnotation(Entity.class);
        }
        String parentTableName = "";
        if (parentAnnotation != null)
            parentTableName = parentAnnotation.tableName();

        queryBuilder.append("FOREIGN KEY(");

        String columnSeparator = "";
        for (String childColumn : foreignKey.childColumns()) {
            queryBuilder.append(columnSeparator);
            queryBuilder.append("`");
            queryBuilder.append(childColumn);
            queryBuilder.append("`");
            columnSeparator = ", ";
        }
        queryBuilder.append(") REFERENCES `");
        queryBuilder.append(parentTableName);
        queryBuilder.append("`(");

        columnSeparator = "";
        for (String parentColumn : foreignKey.parentColumns()) {
            queryBuilder.append(columnSeparator);
            queryBuilder.append("`");
            queryBuilder.append(parentColumn);
            queryBuilder.append("`");
            columnSeparator = ", ";
        }
        queryBuilder.append(") ON UPDATE NO ACTION ON DELETE ");
        String onDeleteAction = foreignKey.onDelete() == ForeignKey.CASCADE ? "CASCADE" : "NO ACTION";
        queryBuilder.append(onDeleteAction);
    }

    private String processIndex(IndexEntry indexEntry) {
        StringBuilder queryBuilder = new StringBuilder("CREATE INDEX IF NOT EXISTS `index_");
        queryBuilder.append(indexEntry.getEntityAnnotation().tableName());
        queryBuilder.append("_");
        queryBuilder.append(indexEntry.getColumnAnnotation().name());
        queryBuilder.append("` ON `");
        queryBuilder.append(indexEntry.getEntityAnnotation().tableName());
        queryBuilder.append("` (`");
        queryBuilder.append(indexEntry.getColumnAnnotation().name());
        queryBuilder.append("`)");
        return queryBuilder.toString();
    }
}
