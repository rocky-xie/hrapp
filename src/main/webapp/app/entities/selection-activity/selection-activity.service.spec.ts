import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { SelectionActivity } from '@/shared/model/selection-activity.model';

import SelectionActivityService from './selection-activity.service';

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
  describe('SelectionActivity Service', () => {
    let service: SelectionActivityService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new SelectionActivityService();
      currentDate = new Date();
      elemDefault = new SelectionActivity(
        123,
        'RESUME_SCREENING',
        'WRITTEN_TEST',
        currentDate,
        currentDate,
        0,
        'AAAAAAA',
        'AAAAAAA',
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a SelectionActivity', async () => {
        const returnedFromService = {
          id: 123,
          startAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { startAt: currentDate, endAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a SelectionActivity', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a SelectionActivity', async () => {
        const returnedFromService = {
          stageType: 'BBBBBB',
          activityType: 'BBBBBB',
          startAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          meetingRoomId: 1,
          location: 'BBBBBB',
          status: 'BBBBBB',
          scheduleVersion: 1,
          changeReason: 'BBBBBB',
          externalCalendarProvider: 'BBBBBB',
          externalEventId: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startAt: currentDate, endAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a SelectionActivity', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a SelectionActivity', async () => {
        const patchObject = {
          stageType: 'BBBBBB',
          activityType: 'BBBBBB',
          startAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          meetingRoomId: 1,
          status: 'BBBBBB',
          changeReason: 'BBBBBB',
          externalCalendarProvider: 'BBBBBB',
          externalEventId: 'BBBBBB',
          ...new SelectionActivity(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startAt: currentDate, endAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a SelectionActivity', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of SelectionActivity', async () => {
        const returnedFromService = {
          stageType: 'BBBBBB',
          activityType: 'BBBBBB',
          startAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          endAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          meetingRoomId: 1,
          location: 'BBBBBB',
          status: 'BBBBBB',
          scheduleVersion: 1,
          changeReason: 'BBBBBB',
          externalCalendarProvider: 'BBBBBB',
          externalEventId: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startAt: currentDate, endAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of SelectionActivity', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a SelectionActivity', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a SelectionActivity', async () => {
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
