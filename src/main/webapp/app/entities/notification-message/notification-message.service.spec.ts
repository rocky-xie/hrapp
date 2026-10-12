import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { NotificationMessage } from '@/shared/model/notification-message.model';

import NotificationMessageService from './notification-message.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('NotificationMessage Service', () => {
    let service: NotificationMessageService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new NotificationMessageService();
      currentDate = new Date();
      elemDefault = new NotificationMessage(
        123,
        'AAAAAAA',
        0,
        'WAITING_CONFIRMATION',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          confirmedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sentAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a NotificationMessage', async () => {
        const returnedFromService = {
          id: 123,
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          confirmedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sentAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { createdAt: currentDate, confirmedAt: currentDate, sentAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a NotificationMessage', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a NotificationMessage', async () => {
        const returnedFromService = {
          eventType: 'BBBBBB',
          templateId: 1,
          status: 'BBBBBB',
          sendMode: 'BBBBBB',
          finalSubject: 'BBBBBB',
          finalBody: 'BBBBBB',
          recipientsJson: 'BBBBBB',
          retryCount: 1,
          maxRetries: 1,
          lastError: 'BBBBBB',
          createdBy: 'BBBBBB',
          confirmedBy: 'BBBBBB',
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          confirmedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sentAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };

        const expected = { createdAt: currentDate, confirmedAt: currentDate, sentAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a NotificationMessage', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a NotificationMessage', async () => {
        const patchObject = {
          eventType: 'BBBBBB',
          templateId: 1,
          retryCount: 1,
          maxRetries: 1,
          lastError: 'BBBBBB',
          createdBy: 'BBBBBB',
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sentAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...new NotificationMessage(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { createdAt: currentDate, confirmedAt: currentDate, sentAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a NotificationMessage', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of NotificationMessage', async () => {
        const returnedFromService = {
          eventType: 'BBBBBB',
          templateId: 1,
          status: 'BBBBBB',
          sendMode: 'BBBBBB',
          finalSubject: 'BBBBBB',
          finalBody: 'BBBBBB',
          recipientsJson: 'BBBBBB',
          retryCount: 1,
          maxRetries: 1,
          lastError: 'BBBBBB',
          createdBy: 'BBBBBB',
          confirmedBy: 'BBBBBB',
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          confirmedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sentAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { createdAt: currentDate, confirmedAt: currentDate, sentAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of NotificationMessage', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a NotificationMessage', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a NotificationMessage', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
