package ca.wave.web.mvc;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.wave.web.form.Analysis;
import ca.wave.web.util.Constants;

@Controller
@RequestMapping("/report")
public class ReportController {
	private static Log log = LogFactory.getLog(ReportController.class);

	@Value("${wait_page_countdown_start}")
	private Long countdownStart;

	@Value("${wait_page_refresh_interval}")
	private Long refreshInterval;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public String welcome(Model model, HttpServletRequest request, HttpSession session, @RequestParam String id) {
		log.debug("welcome()");

		@SuppressWarnings("unchecked")
		Map<String, Analysis> lm = (Map<String, Analysis>) session.getAttribute(Constants.SESSION_CIRCUIT_ANALYSIS);
		if (lm == null || id == null) {
			model.addAttribute("errorMessage", messageSource.getMessage("error.analysis.not.found", null, request.getLocale()));
			return "error";
		}

		Analysis a = lm.get(id);

		long timeLeft = a.getTimeLeft();
		if (timeLeft > 0L) {
			timeLeft -= refreshInterval;
			if (timeLeft < 0L) {
				timeLeft = 0L;
			}
			a.setTimeLeft(timeLeft);
		}

		File file = new File(a.getReportPath());
		if (file.canWrite()) {

			ServletContext sc = session.getServletContext();
			synchronized (sc) {
				@SuppressWarnings("unchecked")
				Map<String, Analysis> m = (Map<String, Analysis>) sc.getAttribute(Constants.APPLICATION_CIRCUIT_ANALYSIS);
				if (m != null) {
					m.remove(a.getCircuitId());
					log.debug("Circuit analysis is removed from the application map, id = " + a.getCircuitId());
				}
			}

			a.setTimeLeft(0L);
			model.addAttribute("analysis", a);
			return "report";
		} else if (timeLeft == 0L) {
			model.addAttribute("errorMessage", messageSource.getMessage("error.analysis.timeout", new Object[] {countdownStart}, request.getLocale()));
			return "error";
		} else {
			model.addAttribute("analysis", a);
			model.addAttribute("refreshInterval", refreshInterval);
			return "wait";
		}
	}
}