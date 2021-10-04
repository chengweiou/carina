package chengweiou.universe.carina.base.dao;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.ibatis.jdbc.SQL;

import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.base.entity.DtoKey;

public class BaseDaoImpl<T> {
    public String save(T e) {
        List<Field> fieldList = Stream.of(
                    Arrays.asList(e.getClass().getDeclaredFields()),
                    Arrays.asList(e.getClass().getSuperclass().getDeclaredFields())
                ).flatMap(List::stream)
                .filter(field -> {
                    field.setAccessible(true);
                    try {
                        return !Modifier.isStatic(field.getModifiers()) && field.get(e)!=null;
                    } catch (IllegalArgumentException | IllegalAccessException e1) {
                        e1.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
                System.out.println(fieldList);
        return new SQL() {{
            INSERT_INTO(getTable(e));
            VALUES(
                    fieldList.stream().map(Field::getName).collect(Collectors.joining(",")),
                    fieldList.stream().map(f -> "#{"+f.getName()+"}").collect(Collectors.joining(","))
            );
        }}.toString();
    }

    public String delete(T e) {
        return new SQL() {{
            DELETE_FROM(getTable(e)); WHERE("id=#{id}");
        }}.toString();
    }

    public String update(T e) {
        List<Field> fieldList = Stream.of(
                    Arrays.asList(e.getClass().getDeclaredFields()),
                    Arrays.asList(e.getClass().getSuperclass().getDeclaredFields())
                ).flatMap(List::stream)
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(f -> !f.getName().equals("id") && !f.getName().equals("createAt"))
                .filter(f -> {
                    try {
                        f.setAccessible(true);
                        return f.get(e) != null;
                    } catch (IllegalAccessException ex) {
                        LogUtil.e("访问" + e.getClass().getSimpleName() + "中属性："+f.getName(), ex);
                        return false;
                    }
                })
                .collect(Collectors.toList());

        return new SQL() {{
            UPDATE(getTable(e));
            for (Field f : fieldList) {
                SET(f.getName() + "=#{"+f.getName()+"}");
            }
            WHERE("id=#{id}");
        }}.toString();
    }

    public String findById(T e) {
        return new SQL() {{
            SELECT("*"); FROM(getTable(e));
            WHERE("id=#{id}");
        }}.toString();
    }

    public String countByKey(T e) {
        List<String> fieldNameList = Arrays.asList(e.getClass().getDeclaredFields()).stream().filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.isAnnotationPresent(DtoKey.class))
                .map(Field::getName)
                .collect(Collectors.toList());
        return new SQL() {{
            SELECT("count(*)"); FROM(getTable(e));
            WHERE(
                fieldNameList.stream().map(name -> name+"=#{"+name+"}").collect(Collectors.joining(" and "))
            );
        }}.toString();
    }

    public String findByKey(T e) {
        List<String> fieldNameList = Arrays.asList(e.getClass().getDeclaredFields()).stream().filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.isAnnotationPresent(DtoKey.class))
                .map(Field::getName)
                .collect(Collectors.toList());
        return new SQL() {{
            SELECT("*"); FROM(getTable(e));
            WHERE(
                fieldNameList.stream().map(name -> name+"=#{"+name+"}").collect(Collectors.joining(" and "))
            );
        }}.toString();
    }
// // todo null 获取不到类型
//     public String count(SearchCondition searchCondition, T sample) {
//         return baseFind(searchCondition, sample).SELECT("count(*)").toString();
//     }
//     public String find(SearchCondition searchCondition, T sample) {
//         return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
//     }
//     public SQL baseFind(SearchCondition searchCondition, T sample) {
//         return new SQL() {{
//             FROM(getTable(sample));
//         }};
//     }

    private String getTable(T e) {
        String name = e.getClass().getName();
        // 遇到内部静态类，采用外部类名称
        int end = name.contains("$") ? name.lastIndexOf("$") : name.length();
        name = name.substring(name.lastIndexOf(".")+1, end);
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
