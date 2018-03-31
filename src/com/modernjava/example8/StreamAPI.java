package com.modernjava.example8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
					.sorted(Comparator.comparingInt(x -> x.getDistance()))
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
		System.out.println(Shops.stream()
        	.filter(p -> p.getName().length() > 1)
        	.collect(Collectors.toList()));
		/*
		 * All Shops
		 * */
		System.out.println(Shops.stream()
					.map(p -> p.getName())
					.findFirst());
		/*
		 * Optional[店1]
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
    	
	}

}

