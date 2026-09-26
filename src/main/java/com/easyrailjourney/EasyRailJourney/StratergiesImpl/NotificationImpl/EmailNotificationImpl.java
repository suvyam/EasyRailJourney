package com.easyrailjourney.EasyRailJourney.StratergiesImpl.NotificationImpl;


import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.NotificationStratergy;
import com.easyrailjourney.EasyRailJourney.enums.NotificationType;
import com.easyrailjourney.EasyRailJourney.models.Ticket;
import com.easyrailjourney.EasyRailJourney.models.TicketPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailNotificationImpl
        implements NotificationStratergy {


    private final JavaMailSender javaMailSender;

    private final BookingsRepo bookingsRepo;


    public EmailNotificationImpl(
            JavaMailSender javaMailSender,
            BookingsRepo bookingsRepo) {

        this.javaMailSender =
                javaMailSender;

        this.bookingsRepo =
                bookingsRepo;
    }


    // =====================================================
    // MATCH
    // =====================================================

    @Override
    public boolean isMatch(
            NotificationDto notificationDto) {

        return NotificationType.EMAIL.equals(
                notificationDto.getType()
        );
    }


    // =====================================================
    // SEND
    // =====================================================

    @Override
    public boolean send(
            NotificationDto notificationDto) {

        try {

            MimeMessage message =
                    javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );


            helper.setTo(
                    notificationDto.getTo()
            );


            /*
             * This key identifies one logical notification.
             *
             * Example:
             *
             * RESCHEDULE-15-BOOKING-100
             */
            String notificationKey =
                    notificationDto.getNotificationKey();

            System.out.println(
                    "Notification key = "
                            + notificationKey
            );


            // =================================================
            // RESCHEDULE NOTIFICATION
            // =================================================

            if (notificationDto.getTicket() == null) {

                helper.setSubject(
                        "EasyRailJourney - Schedule Change"
                );


                String text =
                        notificationDto.getText();


                if (text == null
                        || text.isBlank()) {

                    text =
                            "Your train schedule has been changed.";
                }


                helper.setText(
                        text.replace(
                                "\n",
                                "<br>"
                        ),
                        true
                );


                javaMailSender.send(
                        message
                );


                return true;
            }


            // =================================================
            // BOOKING CONFIRMATION
            // =================================================

            Ticket ticket =
                    notificationDto.getTicket();


            String pnr =
                    notificationDto.getPnr();


            Bookings booking =
                    bookingsRepo.findByPnr(
                            pnr
                    );


            StringBuilder passengerDetails =
                    new StringBuilder();


            if (ticket.getTicketPassenger() != null) {

                for (TicketPassenger tp :
                        ticket.getTicketPassenger()) {

                    passengerDetails.append(
                            "<hr>"
                    );


                    passengerDetails.append(
                            "<p><b>Passenger:</b> "
                                    + tp.getName()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>Age:</b> "
                                    + tp.getAge()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>Train:</b> "
                                    + tp.getTrainName()
                                    + " ("
                                    + tp.getTrainNumber()
                                    + ")</p>"
                    );


                    passengerDetails.append(
                            "<p><b>Class:</b> "
                                    + booking.getTrainClass()
                                            .getClass()
                                            .getName()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>Coach:</b> "
                                    + tp.getCoachNumber()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>Seat:</b> "
                                    + tp.getSeatNumber()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>From:</b> "
                                    + tp.getDepartureStationName()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>To:</b> "
                                    + tp.getArrivalStationName()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>Departure:</b> "
                                    + tp.getDepartureTime()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>Arrival:</b> "
                                    + tp.getArrivalTime()
                                    + "</p>"
                    );


                    passengerDetails.append(
                            "<p><b>Status:</b> "
                                    + tp.getBookingStatus()
                                    + "</p>"
                    );
                }
            }


            helper.setSubject(
                    "EasyRailJourney - Booking Confirmation"
            );


            helper.setText(
                    "<h2>Booking Created</h2>"
                            + "<p>Your booking has been "
                            + "successfully created.</p>"
                            + "<p><b>PNR:</b> "
                            + pnr
                            + "</p>"
                            + passengerDetails,
                    true
            );


            javaMailSender.send(
                    message
            );


            return true;


        } catch (
                MessagingException
                        | MailException e) {

            System.err.println(
                    "Failed to send email: "
                            + e.getMessage()
            );

            return false;
        }
    }
}