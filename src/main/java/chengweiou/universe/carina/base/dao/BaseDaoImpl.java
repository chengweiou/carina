package chengweiou.universe.carina.base.dao;


//@Repository
//public class BaseDaoImpl<T> implements BaseDao<T> {
//    @Autowired
//    private MongoTemplate template;
//    @Override
//    public void save(T e) {
//        template.save(e);
//    }
//
//    @Override
//    public long delete(T e) {
//        return template.remove(e).getDeletedCount();
//    }
//
//
//    @Override
//    public T findById(T e) {
//        String id = "";
//        try {
//            Method method = e.getClass().getDeclaredMethod("getId", null);
//            id = (String) method.invoke(e);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
//            ex.printStackTrace();
//        }
//        return (T) template.findById(id, e.getClass());
//    }
//
//    @Override
//    public long count(SearchCondition searchCondition) {
//        Query query = new Query();
//        if (searchCondition.getK() != null) query.addCriteria(Criteria.where("name").regex(searchCondition.getFull().getReg().getPattern()));
//        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        return template.count(query, entityClass);
//    }
//
//    @Override
//    public List<T> find(SearchCondition searchCondition) {
//        Query query = new Query();
//        if (searchCondition.getK() != null) query.addCriteria(Criteria.where("name").regex(searchCondition.getFull().getReg().getPattern()));
//        query.skip(searchCondition.getSkip()).limit(searchCondition.getLimit()).with(searchCondition.getMongoSort());
//        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        return template.find(query, entityClass);
//    }
//}
