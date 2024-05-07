package media.toloka.rfa.radio.station.onlinelist.Model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class StationReport {

    public class ReportTrack {
        @Expose
        private String starts;
        @Expose
        private String ends;
        @Expose
        private String type;
        @Expose
        private String name;
        @Expose
        private ReportMetadata metadata;
        @Expose
        private String record;
    }

    public class ReportMetadata {
        @Expose
        private String id;
        @Expose
        private String name;
        @Expose
        private String mime;
        @Expose
        private String track_title;
        @Expose
        private String artist_name;
        @Expose
        private String length;
        @Expose
        private String album_title;
        @Expose
        private String genre;
        @Expose
        private String year;
        @Expose
        private String language;

    }

    public class ReportShow {
        @Expose
        private String start_timestamp;
        @Expose
        private String end_timestamp;
        @Expose
        private String name;
        @Expose
        private String description;
        @Expose
        private String starts;
        @Expose
        private String ends;

    }

    @Expose
    private String env;
    @Expose
    private String schedulerTime;
    @Expose
    private ReportTrack previous;
    @Expose
    private ReportTrack current;
    @Expose
    private ReportTrack next;
    @Expose
    private ReportShow currentShow;
    @Expose
    private ReportShow nextShow;
    @Expose
    private String source_enabled;
    @Expose
    private String timezone;
    @Expose
    private String timezoneOffset;

}
