import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { ApplicationEvent } from '@/shared/model/application-event.model';

import ApplicationEventService from './application-event.service';

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
  describe('ApplicationEvent Service', () => {
    let service: ApplicationEventService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ApplicationEventService();
      currentDate = new Date();
      elemDefault = new ApplicationEvent(
        123,
        'RESUME_SCREENING',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { occurredAt: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
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

      it('should create a ApplicationEvent', async () => {
        const returnedFromService = { id: 123, occurredAt: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { occurredAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a ApplicationEvent', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ApplicationEvent', async () => {
        const returnedFromService = {
          stageType: 'BBBBBB',
          eventType: 'BBBBBB',
          previousValue: 'BBBBBB',
          newValue: 'BBBBBB',
          actor: 'BBBBBB',
          occurredAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reason: 'BBBBBB',
          metadataJson: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { occurredAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a ApplicationEvent', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ApplicationEvent', async () => {
        const patchObject = {
          stageType: 'BBBBBB',
          previousValue: 'BBBBBB',
          newValue: 'BBBBBB',
          occurredAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          metadataJson: 'BBBBBB',
          ...new ApplicationEvent(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { occurredAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ApplicationEvent', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ApplicationEvent', async () => {
        const returnedFromService = {
          stageType: 'BBBBBB',
          eventType: 'BBBBBB',
          previousValue: 'BBBBBB',
          newValue: 'BBBBBB',
          actor: 'BBBBBB',
          occurredAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reason: 'BBBBBB',
          metadataJson: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { occurredAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of ApplicationEvent', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ApplicationEvent', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ApplicationEvent', async () => {
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
