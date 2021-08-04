package cn.ex.project.scaffold;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author: Chen GuoLin
 * @create: 2021-04-24
 **/
@Slf4j
@SpringBootApplication
public class ScaffoldApplication {

	public static void main(String[] args) {
		try {
			SpringApplication application = new SpringApplication(ScaffoldApplication.class);
			application.setBannerMode(Banner.Mode.CONSOLE);
			application.run(args);
		} catch (Exception e) {
			log.error("[Application]start Exception",e);
		}
	}
}
