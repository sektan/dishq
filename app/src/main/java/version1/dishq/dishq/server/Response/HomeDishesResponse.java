package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 27-12-2016.
 * Package name version1.dishq.dishq.
 */

public class HomeDishesResponse {

    @SerializedName("data")
    @Expose
    public HomeData homeData;

    @SerializedName("response")
    @Expose
    private String homeResponse;

    public class HomeData {
        @SerializedName("greeting_info")
        @Expose
        public GreetingsInfo greetingsInfo;

        @SerializedName("dish_data")
        @Expose
        public ArrayList<DishDataInfo> dishDataInfos;

        @SerializedName("default_tab_for_eating")
        @Expose
        private String defaultTab;

        @SerializedName("show_greeting_message")
        @Expose
        private Boolean showGreeting;

        public String getDefaultTab() {
            return defaultTab;
        }

        public void setDefaultTab(String defaultTab) {
            this.defaultTab = defaultTab;
        }

        public Boolean getShowGreeting() {
            return showGreeting;
        }

        public void setShowGreeting(Boolean showGreeting) {
            this.showGreeting = showGreeting;
        }
    }

    public class GreetingsInfo {

        @SerializedName("photo")
        @Expose
        private String greetingsPhoto;

        @SerializedName("greeting_message")
        @Expose
        private String greetingMessage;

        @SerializedName("context_message")
        @Expose
        private String contextMessage;

        public String getGreetingsPhoto() {
            return greetingsPhoto;
        }

        public void setGreetingsPhoto(String greetingsPhoto) {
            this.greetingsPhoto = greetingsPhoto;
        }

        public String getGreetingMessage() {
            return greetingMessage;
        }

        public void setGreetingMessage(String greetingMessage) {
            this.greetingMessage = greetingMessage;
        }

        public String getContextMessage() {
            return contextMessage;
        }

        public void setContextMessage(String contextMessage) {
            this.contextMessage = contextMessage;
        }
    }

}
