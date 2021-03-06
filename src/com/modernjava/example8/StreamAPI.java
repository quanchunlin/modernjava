package com.modernjava.example8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.modernjava.Shop;

public class StreamAPI {

	public static void main(String[] args) throws IOException {
		try(final Stream<String> lines = Files.lines(Paths.get("D:\\test.txt"))) {
			System.out.println(
				lines.map(line -> line.split("[\\s]+"))
					.flatMap(Arrays::stream)
					.distinct() //유일한 값 뽑기
					.sorted() //스트링이 컴패어버우이기 때문에 소팅 가능
					.collect(Collectors.toList()) //리스트로 변환 Collectors.joining("\n")
			);
		}
		/* 
		 * [a1, b2, c3, test1, test2]
		 * */
		final String[] longNumberTypeStringArray = "1,2,3,4".split("[\\,]+");
		List<Long> longNumberTypeArray = Arrays.asList(longNumberTypeStringArray)
				.stream()
				.map(Long::valueOf)
				.collect(Collectors.toList());
		System.out.println(longNumberTypeArray);
		/*
		 * [1, 2, 3, 4]
		 * */
		final List<Integer> integerNumberListArray = Arrays.asList(1,2,3,4,5,6);
		final String integerNumberArrayJoinToString = integerNumberListArray.stream()
				.map(String::valueOf) //Integer -> String
				.collect(Collectors.joining(" : "));
		System.out.println(integerNumberArrayJoinToString);
		/*
		 * 1 : 2 : 3 : 4 : 5 : 6
		 * */
		String str = "ABC,DEF";
		System.out.println( Arrays.asList(
					str.split("[\\,]+")
				)
				.stream()
				.map(Shop::new)
				.collect(Collectors.toList())
		);
		/*
		 * [Shop [name=ABC, distance=0], Shop [name=DEF, distance=0]]
		 * */
		Shop s1 = new Shop("店1", 5);
		Shop s2 = new Shop("店2", 2);
		Shop s3 = new Shop("店3", 4);
		List<Shop> Shops = Arrays.asList(s1, s2, s3);
		String name = Shops.stream()
					.sorted(Comparator.comparingInt(x -> x.getDistance())) //离我最近
					//.max(Comparator.comparingInt(x -> x.getDistance())) //最大
					//.min(Comparator.comparingInt(x -> x.getDistance())) //最小
					.findFirst()
					.get()
					.getName();
		System.out.println("距离我最近的店是:" + name);
		/*
		 * 距离我最近的店是:店2
		 * */
		System.out.println(Shops.stream()
        	.filter(p -> p.getDistance() < 4) //过滤
        	.collect(Collectors.toList()));
		/*
		 * [Shop [name=店2, distance=2]]
		 * */
		List<Shop> Shops2 = Shops.stream()
				.sorted(Comparator.comparingInt(x -> x.getDistance())) //离我最近
				.limit(2).collect(Collectors.toList());//前2个
		System.out.println(Shops2);
		/*
		 * [Shop [name=店2, distance=2], Shop [name=店3, distance=4]]
		 * */
		System.out.println(Shops.stream()
        	.filter(p -> p.getName().length() > 1)
        	.collect(Collectors.toList()));
		/*
		 * All Shops
		 * */
		System.out.println(Shops.stream()
					.map(p -> p.getName()) //只获取店名称
					.findFirst());
		/*
		 * Optional[店1]
		 * */
		Map<String, Integer> nameAndDistanceMap = Shops.stream()
				.collect(Collectors.toMap(Shop::getName, Shop::getDistance));//制成 Key -> Value Map
		System.out.println(nameAndDistanceMap);
		/*
		 * {店2=2, 店1=5, 店3=4}
		 * */
		Shop s2_2 = new Shop("店2-2", 2);
		List<Shop> Shops_2 = Arrays.asList(s2_2, s1, s2, s3);
		Map<Integer, List<Shop>> ShopsgroupingBy = Shops_2.stream()
                .collect(Collectors.groupingBy(Shop::getDistance));//分组
		System.out.println(ShopsgroupingBy);
		/*
		 * {
		 * 2=[Shop [name=店2-2, distance=2], Shop [name=店2, distance=2]], 
		 * 4=[Shop [name=店3, distance=4]], 
		 * 5=[Shop [name=店1, distance=5]]
		 * }
		 * */
		
		List<List<String>> lists = new ArrayList<>();
        lists.add(Arrays.asList("11", "222"));
        lists.add(Arrays.asList("aa", "bb", "ccc", "dd"));
        lists.add(Arrays.asList("a11", "a22", "a33"));
        Iterator<String> listStringiterator = lists.stream()
    	        .flatMap(Collection::stream)
    	        .filter(x -> x.length() > 2)
    	        //.count()
        		.iterator();
    	while(listStringiterator.hasNext()){
    		System.out.println(listStringiterator.next());
    	}
    	/*222
		ccc
		a11
		a22
		a33*/
    	/**
    	 * 串行和并行流
    	 * 以上都是 串行流处理(stream())，以下为并行处理(parallelStream())
    	 * */
    	System.out.println(
    			Shops_2.parallelStream()
			        .sorted(Comparator.comparingInt(Shop::getDistance))
			        .map(Shop::getName)
			        .limit(2)
			        .collect(Collectors.toList())
			        /*
			         * [店2-2, 店2]
			         * */
        );
    	
    	//计算集合元素的最大值、最小值、总和以及平均值 summaryStatistics() 
    	//返回 IntSummaryStatistics,LongSummaryStatistics,DoubleSummaryStatistic
    	//获取数字的个数、最小值、最大值、总和以及平均值
    	List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
    	IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
    	System.out.println("Highest prime number in List : " + stats.getMax());//29
    	System.out.println("Lowest prime number in List : " + stats.getMin());//2
    	System.out.println("Sum of all prime numbers : " + stats.getSum());//129
    	System.out.println("Average of all prime numbers : " + stats.getAverage());//12.9
    	
    	int totalReduce = Arrays.asList(1,2,3)
    			.parallelStream()
    			.reduce(0, (i1, i2) -> i1 + i2);//0为初始值
    	System.out.println(totalReduce);//6
    	
    	long start = System.currentTimeMillis();
    	int totalPeekReduce = Arrays.asList(1,2,3)
	    	.parallelStream() //多线程处理 It took 2001
	    	//.stream() //CPU core数 每个2秒 -- It took 6003
	    	.peek(i -> {
	    		try {
	    			TimeUnit.SECONDS.sleep(2); 
	    		}catch (InterruptedException e){
	    			e.printStackTrace();
	    		}
	    	})
	    	.reduce(0, (i1, i2) -> i1 + i2);
	    	System.out.println("It took " + (System.currentTimeMillis() - start));//It took 2001
	    	System.out.println(totalPeekReduce);//6
	}

}


