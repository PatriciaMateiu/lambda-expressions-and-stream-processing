package pack;

import src.main.ActivityCounter;
import src.main.DateCounter;
import src.main.FileWriter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public void countNrOfDays(HashSet<String> dates){

        DateCounter cnt1 = (HashSet<String> l)-> { return l.size();};
        //int c = cnt.count(items);
        System.out.println("Number of days of monitored data : " + cnt1.count(dates));
    }

    public void countActivities(List<String> activities, BufferedWriter bw) throws IOException {

        ActivityCounter cnt2 = (List<String> l)->{ Map<String, Long> result = l.stream()
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
            return result;};

        Map<String, Long> map = cnt2.count(activities);

        map.forEach((part,nr)-> {
            try {
                bw.write(part + "---->" + nr);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
                //System.out.println(part + "---->" + nr));
    }

    public void countActivitiesOnDays(List<MonitoredData> items, BufferedWriter bw) throws IOException {

        Map<String, List<String>> res = items.stream().sorted(Comparator.comparing(MonitoredData::getStart_day)).collect(Collectors.groupingBy(MonitoredData::getStart_day, Collectors.mapping(MonitoredData::getActivity_label, Collectors.toList())));

        res.forEach((day,listt)->{
            try {
                bw.write(day);
                bw.newLine();
                countActivities(listt,bw);
                bw.newLine();
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }});

    }

    public void getTimeIntervals(List<MonitoredData> items, BufferedWriter bw) throws IOException{


        Map result = items.stream().collect(Collectors.toMap(MonitoredData::getLinenr, MonitoredData::getInterval));

        result.forEach((label, interval)->{
            try{
                bw.write(interval.toString());
                bw.newLine();
            }catch (IOException e){
                e.printStackTrace();
            }
        });

    }

    public String transform(long millis){
        long totalSecs = millis/1000;
        long days = totalSecs / 86400;
        long hours = (totalSecs / 3600) % 24;
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;

        String s = "";
        if(days != 0){
            s = s + days + " days ";
        }
        if (hours != 0) {
            s = s + hours + " hours ";
        }
        if(mins != 0){
            s = s + mins + " minutes ";
        }
        if(secs != 0){
            s = s + secs + " seconds ";
        }
        return s;
    }

    public static <K, V> Map<K, V> filterByValue(Map<K, V> map, Predicate<V> predicate) {
        return map.entrySet()
                .stream()
                .filter(x -> predicate.test(x.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public void getEntireDuration(List<MonitoredData> items, BufferedWriter bw) throws IOException{

        Map result = items.stream().collect(Collectors.groupingBy(MonitoredData::getActivity_label,Collectors.mapping(MonitoredData::getMillis, Collectors.toList())));
       // Map result2 = filterByValue(result, x-> Integer.parseInt(x.toString()) < 300000 );

        //result2.forEach((key,val)-> System.out.println(key + "---->" + val));

        result.forEach((label,interval)->{
                try {
                    long sum = 0;
                    String[] spl = interval.toString().substring(1,interval.toString().length()-2).split(", ");
                    for(int i = 0; i < spl.length; i++){
                        //System.out.println(spl[i]);
                        Long l = Long.decode(spl[i]);
                        sum += l;
                    }
                    bw.write(label + "----->" + transform(sum));
                    bw.newLine();
                }catch (IOException e){
                    e.printStackTrace();
                }
        });

    }

    public void getFilteredResults(List<MonitoredData> items, BufferedWriter bw) throws IOException{

        List li = new ArrayList();
        //List result = items.stream().filter(item->MonitoredData::getMillis < 300000).collect(Collectors.groupingBy(MonitoredData::getActivity_label,Collectors.mapping(MonitoredData::getMillis, Collectors.toList())));
        Map result = items.stream().collect(Collectors.groupingBy(MonitoredData::getActivity_label,Collectors.mapping(MonitoredData::getMillis, Collectors.toList())));
        result.forEach((label, listt)->{
            listt = result.get(label);
            String[] it = listt.toString().substring(1, listt.toString().length()-1).split(", ");
            //System.out.println(it.length);
            int nr = 0;
            for(int k = 0; k < it.length; k++){
                long a = Long.decode(it[k]);
                if(a < 300000){
                    nr++;
                    System.out.println(nr);
                }
                if(nr == 0.9*(it.length)){
                    li.add(label.toString());
                }

            }
            //listt = new ArrayList<>(result.entrySet().);

        });
        System.out.println(li);

    }


    public static void main(String args[]) throws IOException {


        Main main = new Main();
        String fileName = "D:\\PT2019\\pt2019_30422_mateiu_patricia\\New folder\\Activities.txt";
        List<String> list = new ArrayList<String>();
        HashSet<String> dates = new HashSet<>();
        List<String> activities = new ArrayList<String>();
        List<MonitoredData>items = new ArrayList<MonitoredData>();

        FileWriter fw = new FileWriter();

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            list = stream.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        final int[] i = {0};
        list.forEach(part->{String[] split = part.split("\\s+");
                            i[0]++;
                            dates.add(split[0]);
                            dates.add(split[2]);
                            activities.add(split[4]);
                            String s1 = split[0] + " " +  split[1];
                            String s2 = split[2] + " " + split[3];
                            items.add(new MonitoredData(s1, s2, split[4], i[0]));});

        main.countNrOfDays(dates);

        BufferedWriter bw = fw.writeInFile("ActivitiesCount");
        main.countActivities(activities, bw);
        bw.close();

        BufferedWriter bw1 = fw.writeInFile("Activities_DaysCount");
        main.countActivitiesOnDays(items,bw1);
        bw1.close();

        BufferedWriter bw2 = fw.writeInFile("RecordedIntervals");
        main.getTimeIntervals(items, bw2);
        bw2.close();

        BufferedWriter bw3 = fw.writeInFile("RecordedDurations");
        main.getEntireDuration(items,bw3);
        bw3.close();

        main.getFilteredResults(items,bw);
        //ll.forEach(elem->System.out.println(elem));
        //List<String>[] set = new ArrayList[14];
        //Date d = items.get(0).getStart_time();
        //List<String> li = new ArrayList<String>();

    }

}
