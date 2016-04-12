package ca.wave.web.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.wave.web.form.Analysis;
import ca.wave.web.form.Circuit;
import ca.wave.web.util.Constants;

@Controller
@RequestMapping("/")
public class LaunchController {
	private static Log log = LogFactory.getLog(LaunchController.class);

	private SecureRandom sr = new SecureRandom();

	@Value("${wait_page_countdown_start}")
	private Long countdownStart;

	@Value("${command_launcher_name}")
	private String commandLauncherName;

	@Value("${command_launcher_parameters}")
	private String commandLauncherParameters;

	@Value("${circuit_analyser_classpath}")
	private String circuitAnalyserClasspath;

	@Value("${circuit_report_location}")
	private String circuitReportLocation;

	@RequestMapping(method = RequestMethod.GET)
	public String welcome(Model model) {
		log.debug("welcome()");
		model.addAttribute(new Circuit());
		return "launch";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String launch(Circuit circuit, BindingResult result, HttpSession session) {
		if (!StringUtils.hasText(circuit.getCircuitId())) {
			result.reject("error.invalid.circuitId");
			return "launch";
		}

		Analysis a = null;
		String filePath = null;
		long now = System.currentTimeMillis();
		ServletContext sc = session.getServletContext();

		synchronized (sc) {
			@SuppressWarnings("unchecked")
			Map<String, Analysis> m = (Map<String, Analysis>) sc.getAttribute(Constants.APPLICATION_CIRCUIT_ANALYSIS);
			if (m == null) {
				m = new HashMap<String, Analysis>();
				sc.setAttribute(Constants.APPLICATION_CIRCUIT_ANALYSIS, m);
				log.debug("Application circuit analysis map is created.");
			}

			a = m.get(circuit.getCircuitId());
			if (a != null) {
				log.debug(a.getLaunchTime());
				log.debug(countdownStart * 1000);
				log.debug(a.getLaunchTime() + (countdownStart * 1000));
				log.debug(now);
			}
			if (a != null && a.getLaunchTime() + countdownStart * 1000 > now) {
				// found an existing analysis in progress
				log.debug("Existing circuit analysis is found in the application map, id = " + circuit.getCircuitId());

				Analysis la = new Analysis();
				la.setAnalysisId(a.getAnalysisId());
				la.setCircuitId(a.getCircuitId());
				la.setLaunchTime(a.getLaunchTime());
				la.setReportPath(a.getReportPath());
				la.setTimeLeft(countdownStart - ((now - a.getLaunchTime()) / 1000));

				@SuppressWarnings("unchecked")
				Map<String, Analysis> lm = (Map<String, Analysis>) session.getAttribute(Constants.SESSION_CIRCUIT_ANALYSIS);
				if (lm == null) {
					lm = new HashMap<String, Analysis>();
					session.setAttribute(Constants.SESSION_CIRCUIT_ANALYSIS, lm);
					log.debug("Session circuit analysis map is created.");
				}
				lm.put(la.getAnalysisId(), la);
				log.debug("Existing circuit analysis is copied to the session map.");

				return "redirect:/report?id=" + la.getAnalysisId();

			} else {
				// new analysis has to be created
				log.debug("New circuit analysis is created in the application map, id = " + circuit.getCircuitId());

				String circuitId = circuit.getCircuitId().replace('/', '_').replace('\\', '_').replace(':', '_');
				filePath = circuitReportLocation.replaceFirst("CIRCUITID", circuitId);
				filePath = filePath.replaceFirst("TIMESTAMP", String.valueOf(now));

				a = new Analysis();
				a.setAnalysisId(String.valueOf(sr.nextLong()));
				a.setCircuitId(circuit.getCircuitId());
				a.setLaunchTime(now);
				a.setReportPath(filePath);
				a.setTimeLeft(countdownStart);

				m.put(a.getCircuitId(), a);
			}
		}

		String [] cmd = new String[3];
		cmd[0] = commandLauncherName;
		cmd[1] = commandLauncherParameters;
		cmd[2] = circuitAnalyserClasspath + " " + circuit.getCircuitId() + " " + filePath;

		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			for (String c : cmd) {
				sb.append(c).append(" ");
			}
			log.debug("Executing an external command: " + sb.toString());
		}

		Process exec = null;
		try {
			exec = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			log.error("Could not execute an external command.", e);
		}

		if (log.isTraceEnabled()) {
			StringBuilder sb = new StringBuilder();
			try {
				Thread.sleep(1000L);
				int bytes;
				InputStream errorStream = exec.getErrorStream();
				if (errorStream.available() > 0) {
					sb.append("\n<ERROR>\n");
					while ((bytes = errorStream.read()) != -1) {
						sb.append((char) bytes);
					}
					sb.append("</ERROR>\n");
				} else {
					InputStream inStream = exec.getInputStream();
					if (inStream.available() > 0) {
						sb.append("\n<OUTPUT>\n");
						while ((bytes = inStream.read()) != -1) {
							sb.append((char) bytes);
						}
						sb.append("</OUTPUT>\n");
					}
				}
				log.debug(sb.toString());
			} catch (InterruptedException e) {
				log.error("Thread.sleep() was interrupted.", e);
			} catch (IOException e) {
				log.error("Could not access exec stream for collecting output.", e);
			}
		}

		Analysis la = new Analysis();
		la.setAnalysisId(a.getAnalysisId());
		la.setCircuitId(a.getCircuitId());
		la.setLaunchTime(a.getLaunchTime());
		la.setReportPath(a.getReportPath());
		la.setTimeLeft(countdownStart);

		@SuppressWarnings("unchecked")
		Map<String, Analysis> lm = (Map<String, Analysis>) session.getAttribute(Constants.SESSION_CIRCUIT_ANALYSIS);
		if (lm == null) {
			lm = new HashMap<String, Analysis>();
			session.setAttribute(Constants.SESSION_CIRCUIT_ANALYSIS, lm);
			log.debug("Session circuit analysis map is created.");
		}
		lm.put(la.getAnalysisId(), la);
		log.debug("New circuit analysis is launched and copied to the session map.");

		return "redirect:/report?id=" + la.getAnalysisId();
	}
}